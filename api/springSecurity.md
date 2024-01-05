Transcrição
Agora que implementamos algumas boas práticas na API, continuaremos com o estudo de outras funcionalidades que realizaremos no projeto.

O foco desta aula será na parte de segurança. Até o momento, não lidamos com essa questão de controle de acesso, autenticação, autorização, entre outras funcionalidades.

Vamos apresentar alguns conceitos e diagramas antes de iniciarmos a parte do código.

Spring Security
O Spring contém um módulo específico para tratar de segurança, conhecido como Spring Security.

Para usarmos no Spring Boot, também vamos utilizar esse mesmo módulo, que já existia antes do Boot, o Spring Security, sendo um módulo dedicado para tratarmos das questões relacionadas com segurança em aplicações.

Essas aplicações podem ser tanto Web quanto uma API Rest, este último sendo o nosso caso. Portanto, esse módulo é completo e contém diversas facilidades e ferramentas para nos auxiliar nesse processo de implementar o mecanismo de autenticação e autorização da aplicação ou API.

Vamos entender o que é o Spring Security.

Objetivos
Autenticação
Autorização (controle de acesso)
Proteção contra-ataques (CSRF, clickjacking, etc.)
Em suma, o Spring Security possui três objetivos. Um deles é providenciar um serviço para customizarmos como será o controle de autenticação no projeto. Isto é, como os usuários efetuam login na aplicação.

Os usuários deverão preencher um formulário? É autenticação via token? Usa algum protocolo? Assim, ele possui uma maior flexibilidade para lidar com diversas possibilidades de aplicar um controle de autenticação.

O Spring Security possui, também, a autorização, sendo o controle de acesso para liberarmos a requisição na API ou para fazermos um controle de permissão.

Por exemplo, temos esses usuários e eles possuem a permissão "A", já estes usuários possuem a permissão "B". Os usuários com a permissão "A" podem acessar as URLs, os que tiverem a permissão "B", além dessas URLs, podem acessar outras URLs.

Com isso, conseguimos fazer um controle do acesso ao nosso sistema.

Há, também, um mecanismo de proteção contra os principais ataques que ocorre em uma aplicação, como o CSRF (Cross Site Request Forgery) e o clickjacking.

São esses os três principais objetivos do Spring Security, nos fornecer uma ferramenta para implementarmos autenticação e autorização no projeto e nos proteger dos principais ataques. Isso para não precisarmos implementar o código que protege a aplicação, sendo que já temos disponível.

No caso da nossa API, o que faremos é o controle de autenticação e autorização, o controle de acesso.

Até o momento, não nos preocupamos com essas questões de segurança. Logo, a nossa API está pública. Isso significa que qualquer pessoa que tiver acesso ao endereço da API, consegue disparar requisições, assim como fizemos usando o Insomnia.

Sabemos o endereço da API, no caso é localhost:8080 - dado que está rodando local na máquina. Porém, após efetuarmos o deploy da aplicação em um servidor, seria o mesmo cenário: caso alguém descubra a endereço, conseguirá enviar requisição para cadastrar um médico ou paciente, etc.

O projeto não deveria ser público, somente os funcionários da clínica deveriam ter acesso. Claro que eles utilizarão o front-end ou aplicativo mobile, mas essas aplicações de clientes irão disparar requisições para a nossa API back-end, e esta deve estar protegida.

A API back-end não deve ser pública, ou seja, receber requisições sem um controle de acesso. A partir disso, entra o Spring Security para nos auxiliar na proteção dessa API no back-end.

ATENÇÃO!

Autenticação em aplicação Web (Stateful) != Autenticação em API Rest (Stateless)

Um detalhe importante é que no caso dos cursos de Spring Boot, estamos desenvolvendo uma API Rest, não uma aplicação Web tradicional.

Essas aplicações desenvolvidas em Java usando o Spring, com o Spring MVC (Model-View-Controller) ou JSF (JavaServer Faces). A nossa ideia é desenvolvermos só o back-end, o front-end é separado e não é o foco deste curso.

O processo de autenticação em uma aplicação Web tradicional é diferente do processo de autenticação em uma API Rest. Em uma aplicação Web, temos um conceito chamado de stateful.

Toda vez que um usuário efetua o login em uma aplicação Web, o servidor armazena o estado. Isto é, cria as sessões e, com isso, consegue identificar cada usuário nas próximas requisições.

Por exemplo, esse usuário é dono de determinada sessão, e esses são os dados de memória deste usuário. Cada usuário possui um espaço na memória. Portanto, o servidor armazena essas sessões, espaços em memória e cada sessão contém os dados específicos de cada usuário.

Esse é o conceito de Stateful, é mantido pelo servidor.

Porém, em uma API Rest, não deveríamos fazer isso, porque um dos conceitos é que ela seja stateless, não armazena estado. Caso o cliente da API dispare uma requisição, o servidor processará essa requisição e devolverá a resposta.

Na próxima requisição, o servidor não sabe identificar quem é que está enviando, ele não armazena essa sessão. Assim, o processo de autenticação funciona um pouco diferente, caso esteja acostumado com a aplicação Web.

Como será o processo de autenticação em uma API? Temos diversas estratégias para lidarmos com a autenticação. Uma delas é usando Tokens, e usaremos o JWT - JSON Web Tokens como protocolo padrão para lidar com o gerenciamento desses tokens - geração e armazenamento de informações nos tokens.

Diagrama de autenticação intitulado "Autenticação". À esquerda, temos um protótipo de uma tela de login no celular com os campos "Login" e "Senha" e um botão "Entrar", na parte inferior central. Este está acompanhado do texto "APP Cliente Front-end/Mobile", abaixo. No centro, um quadrado com o texto "API Rest Backend" acompanhado da numeração 3 e do texto "Gerar Token JWT" abaixo e, à direita, um ícone de banco de dados, com o texto "Banco de dados" contido nele. Os elementos ligam-se entre si com setas e possuem numeração: a tela de login liga-se e aponta para "API Rest", referente a seta número 1 nomeada "HTTP Request". "API Rest Backend" aponta para a tela de login, referente a seta número 4, nomeada "Devolver Token JWT". "API Rest Backend" se liga e aponta para o "Banco de dados", referente a seta número 2 acompanhada do texto "SQL Select". Entre as setas número 1 e 4 há um trecho de um código com os campos login e senha: {"login":"fulano@email.com" "senha": "12345678"}.
Esse diagrama contém um esquema do processo de autenticação na API. Lembrando que estamos focando no back-end, e não no front-end. Esta será outra aplicação, podendo ser Web ou Mobile.

No diagrama, o cliente da API seria um aplicativo mobile. Assim, quando o funcionário da clínica for abrir o aplicativo, será exibida uma tela de login tradicional, com os campos "Login" e "Senha" com um botão "Entrar", para enviar o processo de autenticação.

O usuário digita o login e senha, e clica no botão para enviar. Deste modo, a aplicação captura esses dados e dispara uma requisição para a API back-end - da mesma forma que enviamos pelo Insomnia.

Logo, o primeiro passo é a requisição ser disparada pelo aplicativo para a nossa API, e no corpo desta requisição é exibido o JSON com o login e senha digitados na tela de login.

O segundo passo é capturar esse login e senha e verificar se o usuário está cadastrado no sistema, isto é, teremos que consultar o banco de dados. Por isso, precisaremos ter uma tabela em que vamos armazenar os usuários e suas respectivas senhas, que podem acessar a API.

Da mesma maneira que temos uma tabela para armazenar os médicos e outra para os pacientes, teremos uma para guardar os usuários. Logo, o segundo passo do processo de autenticação é: a nossa API capturar esse login e senha, e ir ao banco de dados efetuar uma consulta para verificar a existência dos dados desse usuário.

Se for válido, a API gera um Token, que nada mais é que uma string. A geração desse Token segue o formato JWT, e esse token é devolvido na resposta para a aplicação de cliente, sendo quem disparou a requisição.

Esse é o processo de uma requisição para efetuar o login e autenticar em uma API Rest, usando tokens. Será esse processo que seguiremos neste curso.

Isto é, teremos um controller mapeando a URL de autenticação, receberemos um DTO com os dados do login e faremos uma consulta no banco de dados. Se tiver tudo certo, geramos um token e devolvemos para o front-end, para o cliente que disparou a requisição.

Esse token deve ser armazenado pelo aplicativo mobile/front-end. Há técnicas para guardar isso de forma segura, porque esse token que identifica se o usuário está logado.

Assim, nas requisições seguintes entra o processo de autorização, que consta no diagrama a seguir:

Diagrama de autorização intitulado "Autorização". À esquerda, temos um protótipo de uma tela no celular com os campos "Login" e "CRM" e um botão "Salvar", na parte inferior central. Este está acompanhado do texto "APP Cliente Front-end/Mobile", abaixo. À direita, temos um quadrado com o texto "API Rest Backend". A tela de celular liga-se ao quadrado "API Rest Backend" por uma seta com a numeração 1 e o texto "HTTP Request". "API Rest Backend" liga-se à tela com uma seta. Entre essas setas há um trecho do header e do body. Sendo o header: authorization: Bearer Token_JWT, e body: {"nome":"Fulano Silva" "crm":"000111"}. Abaixo de "API Rest Backend" temos a numeração 2 e o texto "Validar Token JWT", que possui uma seta apontando para um fluxograma estruturado por um losango de decisão com o escrito "3 Token validado?". Esse losango possui duas setas, uma apontando para a direita e outra para a esquerda. A seta direcionada para a esquerda possui o texto "Não" e aponta para um retângulo com o escrito "Bloquear requisição". A seta direcionada para a direita contém o texto "Sim" e aponta para um retângulo com o texto "Liberar requisição".
Na requisição de cadastrar um médico, o aplicativo exibe o formulário de cadastro de médico - simplificamos no diagrama, mas considere que é um formulário completo - e após preenchermos os dados, clicamos no botão "Salvar".

Será disparada uma requisição para a nossa API - da mesma forma que fizemos no Insomnia. No entanto, além de enviar o JSON com os dados do médico no corpo da resposta, a requisição deve incluir um cabeçalho chamado authorization. Neste cabeçalho, levamos o token obtido no processo anterior, de login.

A diferença será essa: todas as URLs e requisições que desejarmos proteger, teremos que validar se na requisição está vindo o cabeçalho authorization com um token. E precisamos validar este token, gerado pela nossa API.

Portanto, o processo de autorização é: primeiro, chega uma requisição na API e ela lê o cabeçalho authorization, captura o token enviado e valida se foi gerado pela API. Teremos um código para verificar a validade do token.

Caso não seja válido, a requisição é interrompida ou bloqueada. Não chamamos o controller para salvar os dados do médico no banco de dados, devolvemos um erro 403 ou 401. Há protocolos HTTP específicos para esse cenário de autenticação e autorização.

Pelo fato do token estar vindo, o usuário já está logado. Portanto, o usuário foi logado previamente e recebeu o token. Este token informa se o login foi efetuado ou não. Caso seja válido, seguimos com o fluxo da requisição.

O processo de autorização funciona assim justamente porque a nossa API deve ser Stateless. Ou seja, não armazena estado e não temos uma sessão informando se o usuário está logado ou não. É como se em cada requisição tivéssemos que logar o usuário.

Todavia, seria incomum enviar usuário e senha em todas as requisições. Para não precisarmos fazer isso, criamos uma URL para realizar a autenticação (onde é enviado o login e senha), e se estiver tudo certo a API gera um token e devolve para o front-end ou para o aplicativo mobile.

Assim, nas próximas requisições o aplicativo leva na requisição, além dos dados em si, o token. Logo, não é necessário mandar login e senha, somente o token. E nesta string, contém a informação de quem é esse usuário e, com isso, a API consegue recuperar esses dados.

Essa é uma das formas de fazer a autenticação em uma API Rest.

Caso já tenha aprendido a desenvolver uma aplicação Web tradicional, o processo de autenticação em uma API Rest é diferente. Não possui o conceito de sessão e cookies, é stateless - cada requisição é individual e não armazena o estado da requisição anterior.

Como o servidor sabe se estamos logados ou não? O cliente precisa enviar alguma coisa para não precisarmos enviar login e senha em toda requisição. Ele informa o login e a senha na requisição de logar, recebe um token e nas próximas ele direciona esse mesmo token.

Nas próximas aulas adicionaremos o Spring Security, e implementar esse conceito de autenticação e autorização analisando cada detalhe no Spring Boot.