# AuctionRMI

uruchomienie: 

1. uruchomić rmiregistry w java\bin rmiregistry.exe -J-Djava.rmi.server.useCodebaseOnly=false
2. uruchomić server - w VM arguments podać: 
                -Djava.security.policy=policy
                -Djava.rmi.server.codebase=file:C:/sciezka_do_projektu/bin/
                -Djava.rmi.server.useCodebaseOnly=false
2. uruchomić klienta - w VM arguments podać: 
                -Djava.security.policy=policy
                -Djava.rmi.server.codebase=file:C:/sciezka_do_projektu/bin/
                -Djava.rmi.server.useCodebaseOnly=false
