# Autosalon
## Вопросы и наблюдения 
### wait() - notify()
1. Перед тем как отпустить монитор, поток засыпает. Это для безопасной остановки, чтобы не потерять данные?
Либо, возможно, это для того, чтобы монитор не висел в воздухе, пока его пытается занять другой поток? 
sleep() у потока перед перед тем как отдать монитор виден на visualVM (тут исп-я notifyAll()) 
![notifyAll](https://user-images.githubusercontent.com/100852484/178044566-684b15b7-b666-4235-9774-8ad93108146f.jpg)
2. На том же изображении видно, что при использовании notifyAll() поток-1 всегда первый получает доступ.
Это значит, что монитор biased (зарезервировался) под него? Однако в данном случае виден явный contention, и, возможно, планиролвщик ОС
отдает приоритет первому потоку? или  тут недостаточный contention, чтобы раздуть монитор? Тогда монитор получается в состоянии thin? 
### Lock
1. Используя интерфейс Lock роль монитора выполняет Condition? Если нет почему на графиках не видно попыток захватить монитор?
![signalAll-fair](https://user-images.githubusercontent.com/100852484/178050227-c3e31259-af2a-4173-b213-d3f4b73acc7b.jpg)
2. Получается, что при использовании честной блокировки, очередь формируется в зависимости от того в каком порядке потоки заходили в первый раз?
