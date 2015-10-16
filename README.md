# McIntegrador
Para utilizar o McIntegrador é necessário 3 passos.

###1 - Configurar arquivo integrador.properties###

Arquivo com as informações necessárias para a integração: acesso ao banco de dados e chave de integração com o McFile. Fica dentro do pacote util do projeto.

###2 - Criar classe de integração e configurar Log###

Estender classe IntegradorBase e implementar os métodos abstratos obrigatórios. Para mais informações sobre os métodos e utilização, consultar a classe de exemplo integracao.IntegradorExemplo.

Após a criação da classe, necessário instanciá-la e retorná-la no método montaIntegrador() dentro da Classe IniciaMcIntegrador. A classe de exemplo já está sendo retornada.

Para configurar o log, basta preencher o arquivo log4j.properties com o caminho e configurações desejadas. Esse arquivo deve ficar na mesma pasta do McIntegrador.jar.

###3 - Configurar o McIntegrador como Serviço###

Na IDE para Java, Eclipse, gerar um runnable jar do projeto com a opção “extract required libraries into generated jar”. Importante mencionar que o integrador.properties pode ficar dentro do jar (no pacote util) ou do lado de fora na mesma pasta. O programa tenta buscar primeiramente do lado de fora, se não achar procura dentro.

Após isso, para iniciar o mesmo como um serviço basta configurar (seguir o LEIA-ME) e executar .bat existente <a href='https://s3.amazonaws.com/helpscout.net/docs/assets/54743955e4b0f6394183bb9e/attachments/5621414d903360610fc68e38/McFile.rar'>neste RAR</a>. (Em modo administrador) Nesse pacote já existe um McIntegrador utilizando o IntegradorExemplo, que acessa banco de dados. Mas o mesmo pode ser substítuido por outro gerado.

<b>Obs</b>: O prunsrv.exe da pasta raiz do rar executa somente para java de 32 bits. Para versões 64 bits necessário utilizar o existente na pasta AMD64.<br/>

<b>Obs2</b>: Para apagar o serviço (caso tenha sido criado errado), executar sc delete McIntegrador

<b>Obs3</b>: Para mais detalhes e informações, visitar:<br/>
  http://joerglenhard.wordpress.com/2012/05/29/build-windows-service-from-java-application-with-procrun/<br/>
  http://commons.apache.org/proper/commons-daemon/procrun.html
