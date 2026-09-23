
Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2026-09-23T17:31:39.915+02:00 ERROR 33972 --- [medical-clinic] [           main] o.s.b.d.LoggingFailureAnalysisReporter   :

***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in com.github.erneo97.medical_clinic.service.VisitReminderService required a single bean, but 2 were found:
- emailReminderSender: defined in file [C:\Users\Michal Kaniewski\Desktop\Kurs Java\medical-clinic\target\classes\com\github\erneo97\medical_clinic\notification\EmailReminderSender.class]
- smsReminderSender: defined in file [C:\Users\Michal Kaniewski\Desktop\Kurs Java\medical-clinic\target\classes\com\github\erneo97\medical_clinic\notification\SmsReminderSender.class]

This may be due to missing parameter name information

Action:

Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed

Ensure that your compiler is configured to use the '-parameters' flag.
You may need to update both your build tool settings as well as your IDE.
(See https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-6.1-Release-Notes#parameter-name-retention)

Sewris VisidReminderService oczekuje sendera który implementuję interfejs RemindSender i komponentu w jednym, mamy dwie klasy spełniające te warunki i niedoprecyzowane czy któraś ma prymat nad inną. Komunikat nam to tłumaczy

Primary określa która implementacja RemindServis jest domyślą nawet gdy występuje kilka.

