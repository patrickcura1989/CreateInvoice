Spool .\dbschemas.log;
	@db_users.sql
Spool off;
connect ofb/ofb
spool .\ofb.log;
	@ofb.sql
spool off;
spool .\ofb_template_data.log;
	@ofb_template_data.sql
spool off;


exit