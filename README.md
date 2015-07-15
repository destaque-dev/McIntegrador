# McIntegrador
Para utilizar o McIntegrador é necessário 3 passos.

###1 - Configurar arquivo integrador.properties###

Arquivo com as informações necessárias para a integração: acesso ao banco de dados e ao McFile. Fica dentro do pacote util do projeto.

###2 - Criar classe de integração###

Extender classe IntegradorBase e implementar os métodos abstratos obrigatórios. Para mais informações sobre os métodos, consultar o JavaDoc.

Após a criação da classe, necessário instanciá-la e retorná-la no método montaIntegrador() dentro da Classe IniciaMcIntegrador.

###3 - Configurar o McIntegrador como Serviço###

Gerar um runnable jar do projeto com a opção “extract required libraries into generated jar”. Importante mencionar que o integrador.properties pode ficar dentro do jar (no pacote util) ou do lado de fora na mesma pasta. O programa tenta buscar primeiramente do lado de fora, se não achar procura dentro.

Após isso, para iniciar o mesmo como um serviço basta configurar e executar .bat existente no rar abaixo. (Em linha de comando em modo de administrador)

https://s3.amazonaws.com/helpscout.net/docs/assets/54743955e4b0f6394183bb9e/attachments/55a6bdd9e4b03e788eda3916/apache_commons_daemon.rar

Necessário configurar dentro do BAT:

Caminho do prunsrv.exe (PR_INSTALL)

Caminho dos logs (PR_LOGPATH)

Caminho do McIntegrador.jar gerado (PR_CLASSPATH)

Caminho do jvm.dll do java no servidor. (PR_JVM) (Necessário Java 5 ou superior)

Obs: O prunsrv.exe da pasta raiz do rar executa somente para java de 32 bits. Para versões 64 bits necessário escolher versão AMD ou IA (Intel).

Obs2: Para apagar o serviço (caso tenha sido criado errado), executar sc delete McIntegrador

Obs3: Para mais detalhes e informações, visitar:
http://joerglenhard.wordpress.com/2012/05/29/build-windows-service-from-java-application-with-procrun/

http://commons.apache.org/proper/commons-daemon/procrun.html
