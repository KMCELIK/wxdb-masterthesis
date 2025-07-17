package de.wxdb.wxdb_masterthesis.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.wxdb.wxdb_masterthesis.DailyImportJob;

@Configuration
public class QuartzConfig {

	@Autowired
	private ImportScheduleProperties scheduleProperties;

	@Bean
	JobDetail dailyJobDetail() {
		JobDetail detail = JobBuilder.newJob(DailyImportJob.class).withIdentity("dailyImportJob").storeDurably()
				.build();
		return detail;
	}

	@Bean
	Trigger dailyJobTrigger(JobDetail dailyJobDetail) {
		String cron = scheduleProperties.getCron();
		return TriggerBuilder.newTrigger().forJob(dailyJobDetail).withIdentity("dailyImportTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
	}
}