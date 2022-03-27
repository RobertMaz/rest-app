package ru.demo.app.restapp.quartz;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzConfiguration {

  private static final int TIME_INTERVAL_SECONDS = 20;

  @Bean
  public Trigger trigger(JobDetail job) {
    return TriggerBuilder
        .newTrigger()
        .forJob(job)
        .withIdentity("Qrtz_Trigger")
        .withDescription("Increasing cash")
        .withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(TIME_INTERVAL_SECONDS))
        .build();
  }

  @Bean
  public JobDetailFactoryBean jobDetail() {
    JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
    jobDetailFactory.setJobClass(IncreaseCashJob.class);
    jobDetailFactory.setDescription("Invoke increasing cash Job service...");
    jobDetailFactory.setDurability(true);
    return jobDetailFactory;
  }

}
