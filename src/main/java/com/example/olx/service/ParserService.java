package com.example.olx.service;

import com.example.olx.configuration.Config;
import com.example.olx.model.*;
import com.example.olx.repository.AdvertisementDashboardRepository;
import com.example.olx.repository.AdvertisementDetailsRepository;
import com.example.olx.repository.SellerRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ParserService {

    @Autowired
    AdvertisementDashboardRepository dashboardRepository;
    @Autowired
    AdvertisementDetailsRepository advertisementDetailsRepository;
    @Autowired
    SellerRepository sellerRepository;

    public static List<CompletableFuture> detailsFutures = new ArrayList<>();

    public static Map<String, AdvertisementDetail> identity = new HashMap();
    private static List<Seller> sellerSet = new ArrayList<>();
    private int loop;
    private String url;

    public static LocalDateTime lastParsingDate = LocalDateTime.MIN;

    public void init() {
        advertisementDetailsRepository.findAll().forEach(advertisementDetail -> {
            identity.put(advertisementDetail.getExternalId2(), advertisementDetail);
            if (lastParsingDate.compareTo(advertisementDetail.getParsingDate()) < 0)
                lastParsingDate = advertisementDetail.getParsingDate();
            if (advertisementDetail.getSearchingUrl() != null)
                Config.urlToParse.add(advertisementDetail.getSearchingUrl());
        });

        sellerRepository.findAll().forEach(sellerSet::add);
    }

    public void parse(ParsingRequest request) {

        System.out.printf("**********\r\n loop %d of %s loops started at %s \r\n**********\r\n", request.getLoop(), request.getNumberOfLoops(), LocalDateTime.now());
        parse(request.getUrl());
        System.out.printf("**********\r\n loop %d of %s loops ended at %s \r\n**********\r\n", request.getLoop(), request.getNumberOfLoops(), LocalDateTime.now());
        request.setLoop(request.getLoop() + 1);
    }

    public void parse(String url) {
        this.url = url;
        loop = (int) (Math.random() * Math.pow(10d, 6d));


        try {
            Document doc = Jsoup.connect(url).get();
            Config.urlToParse.add(url);
            int totalPages;
            String newUrl = "";
            try {
                totalPages = Integer.parseInt(doc.select("a[data-cy=page-link-last]").text());
                final String href = doc.select("a[data-cy=page-link-last]").attr("href");
                newUrl = href.replaceAll("page=\\d+", "page=");
            } catch (Exception e) {
                totalPages = 1;
            }

            final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> parse(doc, 1, 1));
            detailsFutures.add(future);

            if (totalPages > 1) {
                for (int i = 2; i <= totalPages; i++) {

                    final String pageUrl = newUrl + i;
                    Document doc2 = Jsoup.connect(pageUrl).get();
                    if (!doc2.baseUri().equalsIgnoreCase(pageUrl)) {
                        break;
                    }
                    int finalI = i;
                    int finalTotalPages = totalPages;

                    final CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> parse(doc2, finalI, finalTotalPages));
                    detailsFutures.add(future1);
                }
            }

        } catch (Exception e) {
            exceptionHandler(e, url, null);
        }

        while (detailsFutures.stream().anyMatch(f -> !f.isDone())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                exceptionHandler(e);
            }
        }
        System.out.println(loop);
    }

    private String parse(Document doc, int i, int finalTotalPages) {

        try {
            final Elements advertisements = doc.select("tr[class].wrap");

            for (Element advertisement : advertisements) {

                final String externalId = advertisement.select("table[data-id]").first().attr("data-id");
                final String price = advertisement.select("p[class].price").first().text();
                final String detailsHref = advertisement.select("a[href].detailsLink").first().attr("href");
                final String advTitle = advertisement.select("a[class].link.linkWithHash.detailsLink").first().text();
                final String mainImage = advertisement.select("img[class].fleft").attr("src");

                final AdvertisementDashboard shortAdvertisementFromDashboard = AdvertisementDashboard.Builder.anAdvertisementDashboard()
                        .advTitle(advTitle)
                        .externalId(externalId)
                        .price(price)
                        .url(detailsHref)
                        .loop(loop)
                        .baseURI(doc.baseUri())
                        .searchUrl(this.url)
                        .build();

                if (!identity.containsKey(externalId)) {
                    identity.put(externalId, null);
                    detailsFutures.add(CompletableFuture.runAsync(() -> parseDetails(shortAdvertisementFromDashboard, doc, mainImage)));
                } else if (identity.get(externalId) != null) {
                    shortAdvertisementFromDashboard.setAdvertisementDetail(identity.get(externalId));
                    dashboardRepository.save(shortAdvertisementFromDashboard);
                }
            }
        } catch (Exception e) {
            exceptionHandler(e);
        }
        System.out.printf("%s %d - %d of %d\r\n%s\r\n", LocalDateTime.now(), loop, i, finalTotalPages, doc.baseUri());
        return doc.baseUri();
    }

    private void parseDetails(AdvertisementDashboard shortAdvertisementFromDashboard, Document doc, String mainImage) {
        long start = System.currentTimeMillis();

        try {
            AdvertisementDetail advertisementDetail = parseFurther(shortAdvertisementFromDashboard, doc, mainImage);

            identity.put(advertisementDetail.getExternalId2(), advertisementDetailsRepository.save(advertisementDetail));
            shortAdvertisementFromDashboard.setAdvertisementDetail(advertisementDetail);
            dashboardRepository.save(shortAdvertisementFromDashboard);
            lastParsingDate = advertisementDetail.getParsingDate();

            long finish = System.currentTimeMillis();
            if ((finish - start) > 700)
                System.out.printf("mills: %d \r\n%s \r\n", (finish - start), url);

        } catch (Exception e) {
            exceptionHandler(e, url, doc);
        }
    }

    public AdvertisementDetail parseFurther(AdvertisementDashboard shortAdvertisementFromDashboard, Document doc, String mainImage) throws IOException {
        Seller seller;
        final String url = shortAdvertisementFromDashboard.getUrl();

        Document detailsDoc = Jsoup.connect(url).get();
        String itemDescription = "";
        try {
            itemDescription = detailsDoc.select("div[class].clr.lheight20.large").first().text();
        } catch (Exception e) {
            exceptionHandler(e, url, doc);
        }
        final String externalId2 = detailsDoc.select("div[data-item]").first().attr("data-item");
        final String price = detailsDoc.select("div[class].pricelabel").first().text();
        final String location = detailsDoc.select("address").select("p").text();
        final String title = detailsDoc.select("div[class].offer-titlebox").select("h1").text();

        final Elements em = detailsDoc.select("ul[class].offer-bottombar__items");
        final String publishedFromGadget = em.select("a").text();
        final String publishedDate = em.select("em").text();
        final int viewsNumber = Integer.parseInt(em.select("span[class].offer-bottombar__counter").select("strong").text());

        final String advCategoryAndOther = detailsDoc.select("ul[class].offer-details").text();

        List<Image> images = getImages(detailsDoc);

        final Elements sellerInfo = detailsDoc.select("div[class].offer-sidebar__box");
        final String sellerAddress = sellerInfo.select("div[class].offer-user__location").select("p").text();

        final Elements offerUserDetails = sellerInfo.select("div[class].offer-user__details");
        final String sellerName = offerUserDetails.select("h4").text();

        String sellerId = getSellerId(offerUserDetails);

        final String sellerSinceOnOlx = offerUserDetails.select("span[class].user-since").text();
        seller = new Seller(sellerName, sellerAddress, sellerId, sellerSinceOnOlx);

        synchronized (this) {
            if (!sellerSet.contains(seller))
                sellerSet.add(sellerRepository.save(seller));
        }

        return AdvertisementDetail.Builder.anAdvertisementDetail()
                .advTitle(title)
                .externalId2(externalId2)
                .itemDescription(itemDescription)
                .price(price)
                .url(url)
                .searchingUrl(shortAdvertisementFromDashboard.getSearchUrl())
                .advCategoryAndOther(advCategoryAndOther)
                .location(location)
                .publishedDate(publishedDate)
                .publishedGadget(publishedFromGadget)
                .viewsNumber(viewsNumber)
                .images(images)
                .mainImage(mainImage)
                .seller(sellerSet.get(sellerSet.indexOf(seller)))
                .build();
    }

    private String getSellerId(Elements offerUserDetails) {
        String sellerId;
        try {
            sellerId = offerUserDetails.select("h4").select("a[href]").first().attributes().getIgnoreCase("href");
        } catch (Exception e) {
            sellerId = "no id";
        }
        return sellerId;
    }

    private List<Image> getImages(Document detailsDoc) {
        List<Image> images;
        try {
            images = detailsDoc.getElementById("bigGallery")
                    .select("a[href]")
                    .stream()
                    .map(e -> new Image(e.attr("href")))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            images = Collections.emptyList();
        }
        return images;
    }

    private void exceptionHandler(Throwable e) {
        final StackTraceElement[] stackTrace = e.getStackTrace();
        Arrays.stream(stackTrace).forEach(System.out::println);
        System.out.println(e.getMessage());
    }

    private void exceptionHandler(Throwable e, String url, Document doc) {
        exceptionHandler(e);
        if (doc != null) System.out.println(doc.baseUri());
        System.out.println(url);
    }
}
