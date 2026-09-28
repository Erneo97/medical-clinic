
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

Zadanie D2

Primary określa która implementacja RemindServis jest domyślą nawet gdy występuje kilka.


Zadanie D3

Aplikacja uruchamia się ale pomija @Qualifier("smsReminderSender")   gdy jest on nad polem.

Zadanie D4
1. Czym dla Springa różni się `@Service` od `@Component`, a czym dla czytającego kod?
   
Odpowiedź: Dla czytającego staje się jasne jakie jest przeznaczenie danej klasy bez zapoznawaia się z jej funkcjonalnością (na jakim poziomie jest). 


2. Czemu `JsonMapper` z biblioteki nie może dostać adnotacji stereotypowej i jak
   wprowadza się go do kontenera?

Odpowidź: Ponieważ jest to klasa z zewnętrznej biblioteki do której kodu nie mamy dostępu, możemy to obejśc przy pomocy klasy pomocniczej z adnotacją @Component i metody tej klasy z adnotacją @Bean.

3. Klasa ma `@Component`, ale bean nie powstaje i nie ma żadnego błędu. Od czego zaczniesz?

Odpowiedź: Czy hierarchia katalogów pozwala przy automatycznym skanowaniu na wykrycie nowych elementów - danego komponentu.  

