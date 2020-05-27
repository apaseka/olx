alter table advertisement_dashboard alter column id
	set maxvalue 2147483647;

alter table advertisement_dashboard
	add constraint advertisement_dashboard_advertisement_details_external_id2_fk
		foreign key (external_id) references advertisement_detail (external_id2);
