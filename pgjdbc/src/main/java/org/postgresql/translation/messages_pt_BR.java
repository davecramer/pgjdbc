/* Automatically generated by GNU msgfmt.  Do not modify!  */
package org.postgresql.translation;
public class messages_pt_BR extends java.util.ResourceBundle {
  private static final java.lang.String[] table;
  static {
    java.lang.String[] t = new java.lang.String[794];
    t[0] = "";
    t[1] = "Project-Id-Version: PostgreSQL 8.4\nReport-Msgid-Bugs-To: \nPO-Revision-Date: 2004-10-31 20:48-0300\nLast-Translator: Euler Taveira de Oliveira <euler@timbira.com>\nLanguage-Team: Brazilian Portuguese <pgbr-dev@listas.postgresql.org.br>\nLanguage: pt_BR\nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n";
    t[2] = "Not implemented: 2nd phase commit must be issued using an idle connection. commit xid={0}, currentXid={1}, state={2}, transactionState={3}";
    t[3] = "Não está implementado: efetivação da segunda fase deve ser executada utilizado uma conexão ociosa. commit xid={0}, currentXid={1}, state={2}, transactionState={3}";
    t[4] = "DataSource has been closed.";
    t[5] = "DataSource foi fechado.";
    t[8] = "Invalid flags {0}";
    t[9] = "Marcadores={0} inválidos";
    t[18] = "Where: {0}";
    t[19] = "Onde: {0}";
    t[24] = "Unknown XML Source class: {0}";
    t[25] = "Classe XML Source desconhecida: {0}";
    t[26] = "The connection attempt failed.";
    t[27] = "A tentativa de conexão falhou.";
    t[28] = "Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.";
    t[29] = "Posicionado depois do fim do ResultSet.  Você não pode chamar deleteRow() aqui.";
    t[32] = "Can''t use query methods that take a query string on a PreparedStatement.";
    t[33] = "Não pode utilizar métodos de consulta que pegam uma consulta de um comando preparado.";
    t[36] = "Multiple ResultSets were returned by the query.";
    t[37] = "ResultSets múltiplos foram retornados pela consulta.";
    t[50] = "Too many update results were returned.";
    t[51] = "Muitos resultados de atualização foram retornados.";
    t[58] = "Illegal UTF-8 sequence: initial byte is {0}: {1}";
    t[59] = "Sequência UTF-8 ilegal: byte inicial é {0}: {1}";
    t[66] = "The column name {0} was not found in this ResultSet.";
    t[67] = "A nome da coluna {0} não foi encontrado neste ResultSet.";
    t[70] = "Fastpath call {0} - No result was returned and we expected an integer.";
    t[71] = "Chamada ao Fastpath {0} - Nenhum resultado foi retornado e nós esperávamos um inteiro.";
    t[74] = "Protocol error.  Session setup failed.";
    t[75] = "Erro de Protocolo. Configuração da sessão falhou.";
    t[76] = "A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.";
    t[77] = "Uma função foi declarada mas nenhuma chamada a registerOutParameter (1, <algum_tipo>) foi feita.";
    t[78] = "ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.";
    t[79] = "ResultSets com CONCUR_READ_ONLY concorrentes não podem ser atualizados.";
    t[90] = "LOB positioning offsets start at 1.";
    t[91] = "Deslocamentos da posição de LOB começam em 1.";
    t[92] = "Internal Position: {0}";
    t[93] = "Posição Interna: {0}";
    t[96] = "free() was called on this LOB previously";
    t[97] = "free() já foi chamado neste LOB";
    t[100] = "Cannot change transaction read-only property in the middle of a transaction.";
    t[101] = "Não pode mudar propriedade somente-leitura da transação no meio de uma transação.";
    t[102] = "The JVM claims not to support the {0} encoding.";
    t[103] = "A JVM reclamou que não suporta a codificação {0}.";
    t[108] = "{0} function doesn''t take any argument.";
    t[109] = "função {0} não recebe nenhum argumento.";
    t[112] = "xid must not be null";
    t[113] = "xid não deve ser nulo";
    t[114] = "Connection has been closed.";
    t[115] = "Conexão foi fechada.";
    t[122] = "The server does not support SSL.";
    t[123] = "O servidor não suporta SSL.";
    t[124] = "Custom type maps are not supported.";
    t[125] = "Mapeamento de tipos personalizados não são suportados.";
    t[140] = "Illegal UTF-8 sequence: byte {0} of {1} byte sequence is not 10xxxxxx: {2}";
    t[141] = "Sequência UTF-8 ilegal: byte {0} da sequência de bytes {1} não é 10xxxxxx: {2}";
    t[148] = "Hint: {0}";
    t[149] = "Dica: {0}";
    t[152] = "Unable to find name datatype in the system catalogs.";
    t[153] = "Não foi possível encontrar tipo de dado name nos catálogos do sistema.";
    t[156] = "Unsupported Types value: {0}";
    t[157] = "Valor de Types não é suportado: {0}";
    t[158] = "Unknown type {0}.";
    t[159] = "Tipo desconhecido {0}.";
    t[166] = "{0} function takes two and only two arguments.";
    t[167] = "função {0} recebe somente dois argumentos.";
    t[170] = "Finalizing a Connection that was never closed:";
    t[171] = "Fechando uma Conexão que não foi fechada:";
    t[180] = "The maximum field size must be a value greater than or equal to 0.";
    t[181] = "O tamanho máximo de um campo deve ser um valor maior ou igual a 0.";
    t[186] = "PostgreSQL LOBs can only index to: {0}";
    t[187] = "LOBs do PostgreSQL só podem indexar até: {0}";
    t[194] = "Method {0} is not yet implemented.";
    t[195] = "Método {0} ainda não foi implementado.";
    t[198] = "Error loading default settings from driverconfig.properties";
    t[199] = "Erro ao carregar configurações padrão do driverconfig.properties";
    t[200] = "Results cannot be retrieved from a CallableStatement before it is executed.";
    t[201] = "Resultados não podem ser recuperados de uma função antes dela ser executada.";
    t[202] = "Large Objects may not be used in auto-commit mode.";
    t[203] = "Objetos Grandes não podem ser usados no modo de efetivação automática (auto-commit).";
    t[208] = "Expected command status BEGIN, got {0}.";
    t[209] = "Status do comando BEGIN esperado, recebeu {0}.";
    t[218] = "Invalid fetch direction constant: {0}.";
    t[219] = "Constante de direção da busca é inválida: {0}.";
    t[222] = "{0} function takes three and only three arguments.";
    t[223] = "função {0} recebe três e somente três argumentos.";
    t[226] = "This SQLXML object has already been freed.";
    t[227] = "Este objeto SQLXML já foi liberado.";
    t[228] = "Cannot update the ResultSet because it is either before the start or after the end of the results.";
    t[229] = "Não pode atualizar o ResultSet porque ele está antes do início ou depois do fim dos resultados.";
    t[230] = "The JVM claims not to support the encoding: {0}";
    t[231] = "A JVM reclamou que não suporta a codificação: {0}";
    t[232] = "Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.";
    t[233] = "Parâmetro do tipo {0} foi registrado, mas uma chamada a get{1} (tiposql={2}) foi feita.";
    t[240] = "Cannot establish a savepoint in auto-commit mode.";
    t[241] = "Não pode estabelecer um savepoint no modo de efetivação automática (auto-commit).";
    t[242] = "Cannot retrieve the id of a named savepoint.";
    t[243] = "Não pode recuperar o id de um savepoint com nome.";
    t[244] = "The column index is out of range: {0}, number of columns: {1}.";
    t[245] = "O índice da coluna está fora do intervalo: {0}, número de colunas: {1}.";
    t[250] = "Something unusual has occurred to cause the driver to fail. Please report this exception.";
    t[251] = "Alguma coisa não usual ocorreu para causar a falha do driver. Por favor reporte esta exceção.";
    t[260] = "Cannot cast an instance of {0} to type {1}";
    t[261] = "Não pode converter uma instância de {0} para tipo {1}";
    t[264] = "Unknown Types value.";
    t[265] = "Valor de Types desconhecido.";
    t[266] = "Invalid stream length {0}.";
    t[267] = "Tamanho de dado {0} é inválido.";
    t[272] = "Cannot retrieve the name of an unnamed savepoint.";
    t[273] = "Não pode recuperar o nome de um savepoint sem nome.";
    t[274] = "Unable to translate data into the desired encoding.";
    t[275] = "Não foi possível traduzir dado para codificação desejada.";
    t[276] = "Expected an EOF from server, got: {0}";
    t[277] = "Esperado um EOF do servidor, recebido: {0}";
    t[278] = "Bad value for type {0} : {1}";
    t[279] = "Valor inválido para tipo {0} : {1}";
    t[280] = "The server requested password-based authentication, but no password was provided.";
    t[281] = "O servidor pediu autenticação baseada em senha, mas nenhuma senha foi fornecida.";
    t[286] = "Unable to create SAXResult for SQLXML.";
    t[287] = "Não foi possível criar SAXResult para SQLXML.";
    t[292] = "Error during recover";
    t[293] = "Erro durante recuperação";
    t[294] = "tried to call end without corresponding start call. state={0}, start xid={1}, currentXid={2}, preparedXid={3}";
    t[295] = "tentou executar end sem a chamada ao start correspondente. state={0}, start xid={1}, currentXid={2}, preparedXid={3}";
    t[296] = "Truncation of large objects is only implemented in 8.3 and later servers.";
    t[297] = "Truncar objetos grandes só é implementado por servidores 8.3 ou superiores.";
    t[298] = "This PooledConnection has already been closed.";
    t[299] = "Este PooledConnection já foi fechado.";
    t[302] = "ClientInfo property not supported.";
    t[303] = "propriedade ClientInfo não é suportada.";
    t[306] = "Fetch size must be a value greater to or equal to 0.";
    t[307] = "Tamanho da busca deve ser um valor maior ou igual a 0.";
    t[312] = "A connection could not be made using the requested protocol {0}.";
    t[313] = "A conexão não pode ser feita usando protocolo informado {0}.";
    t[318] = "Unknown XML Result class: {0}";
    t[319] = "Classe XML Result desconhecida: {0}";
    t[322] = "There are no rows in this ResultSet.";
    t[323] = "Não há nenhum registro neste ResultSet.";
    t[324] = "Unexpected command status: {0}.";
    t[325] = "Status do comando inesperado: {0}.";
    t[330] = "Heuristic commit/rollback not supported. forget xid={0}";
    t[331] = "Efetivação/Cancelamento heurístico não é suportado. forget xid={0}";
    t[334] = "Not on the insert row.";
    t[335] = "Não está inserindo um registro.";
    t[336] = "This SQLXML object has already been initialized, so you cannot manipulate it further.";
    t[337] = "Este objeto SQLXML já foi inicializado, então você não pode manipulá-lo depois.";
    t[344] = "Server SQLState: {0}";
    t[345] = "SQLState: {0}";
    t[348] = "The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.";
    t[349] = "O parâmetro do servidor standard_conforming_strings foi definido como {0}. O driver JDBC espera que seja on ou off.";
    t[360] = "The driver currently does not support COPY operations.";
    t[361] = "O driver atualmente não suporta operações COPY.";
    t[364] = "The array index is out of range: {0}, number of elements: {1}.";
    t[365] = "O índice da matriz está fora do intervalo: {0}, número de elementos: {1}.";
    t[374] = "suspend/resume not implemented";
    t[375] = "suspender/recomeçar não está implementado";
    t[378] = "Not implemented: one-phase commit must be issued using the same connection that was used to start it";
    t[379] = "Não está implementado: efetivada da primeira fase deve ser executada utilizando a mesma conexão que foi utilizada para iniciá-la";
    t[380] = "Error during one-phase commit. commit xid={0}";
    t[381] = "Erro durante efetivação de uma fase. commit xid={0}";
    t[398] = "Cannot call cancelRowUpdates() when on the insert row.";
    t[399] = "Não pode chamar cancelRowUpdates() quando estiver inserindo registro.";
    t[400] = "Cannot reference a savepoint after it has been released.";
    t[401] = "Não pode referenciar um savepoint após ele ser descartado.";
    t[402] = "You must specify at least one column value to insert a row.";
    t[403] = "Você deve especificar pelo menos uma coluna para inserir um registro.";
    t[404] = "Unable to determine a value for MaxIndexKeys due to missing system catalog data.";
    t[405] = "Não foi possível determinar um valor para MaxIndexKeys por causa de falta de dados no catálogo do sistema.";
    t[410] = "commit called before end. commit xid={0}, state={1}";
    t[411] = "commit executado antes do end. commit xid={0}, state={1}";
    t[412] = "Illegal UTF-8 sequence: final value is out of range: {0}";
    t[413] = "Sequência UTF-8 ilegal: valor final está fora do intervalo: {0}";
    t[414] = "{0} function takes two or three arguments.";
    t[415] = "função {0} recebe dois ou três argumentos.";
    t[428] = "Unable to convert DOMResult SQLXML data to a string.";
    t[429] = "Não foi possível converter dado SQLXML do DOMResult para uma cadeia de caracteres.";
    t[434] = "Unable to decode xml data.";
    t[435] = "Não foi possível decodificar dado xml.";
    t[440] = "Unexpected error writing large object to database.";
    t[441] = "Erro inesperado ao escrever objeto grande no banco de dados.";
    t[442] = "Zero bytes may not occur in string parameters.";
    t[443] = "Zero bytes não podem ocorrer em parâmetros de cadeia de caracteres.";
    t[444] = "A result was returned when none was expected.";
    t[445] = "Um resultado foi retornado quando nenhum era esperado.";
    t[450] = "ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.";
    t[451] = "ResultSet não é atualizável. A consulta que gerou esse conjunto de resultados deve selecionar somente uma tabela, e deve selecionar todas as chaves primárias daquela tabela. Veja a especificação na API do JDBC 2.1, seção 5.6 para obter mais detalhes.";
    t[454] = "Bind message length {0} too long.  This can be caused by very large or incorrect length specifications on InputStream parameters.";
    t[455] = "Tamanho de mensagem de ligação {0} é muito longo. Isso pode ser causado por especificações de tamanho incorretas ou muito grandes nos parâmetros do InputStream.";
    t[460] = "Statement has been closed.";
    t[461] = "Comando foi fechado.";
    t[462] = "No value specified for parameter {0}.";
    t[463] = "Nenhum valor especificado para parâmetro {0}.";
    t[468] = "The array index is out of range: {0}";
    t[469] = "O índice da matriz está fora do intervalo: {0}";
    t[474] = "Unable to bind parameter values for statement.";
    t[475] = "Não foi possível ligar valores de parâmetro ao comando.";
    t[476] = "Can''t refresh the insert row.";
    t[477] = "Não pode renovar um registro inserido.";
    t[480] = "No primary key found for table {0}.";
    t[481] = "Nenhuma chave primária foi encontrada para tabela {0}.";
    t[482] = "Cannot change transaction isolation level in the middle of a transaction.";
    t[483] = "Não pode mudar nível de isolamento da transação no meio de uma transação.";
    t[498] = "Provided InputStream failed.";
    t[499] = "InputStream fornecido falhou.";
    t[500] = "The parameter index is out of range: {0}, number of parameters: {1}.";
    t[501] = "O índice de parâmetro está fora do intervalo: {0}, número de parâmetros: {1}.";
    t[502] = "The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.";
    t[503] = "O parâmetro do servidor DateStyle foi alterado para {0}. O driver JDBC requer que o DateStyle começe com ISO para operação normal.";
    t[508] = "Connection attempt timed out.";
    t[509] = "Tentativa de conexão falhou.";
    t[512] = "Internal Query: {0}";
    t[513] = "Consulta Interna: {0}";
    t[514] = "Error preparing transaction. prepare xid={0}";
    t[515] = "Erro ao preparar transação. prepare xid={0}";
    t[518] = "The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.";
    t[519] = "O tipo de autenticação {0} não é suportado. Verifique se você configurou o arquivo pg_hba.conf incluindo a subrede ou endereço IP do cliente, e se está utilizando o esquema de autenticação suportado pelo driver.";
    t[526] = "Interval {0} not yet implemented";
    t[527] = "Intervalo {0} ainda não foi implementado";
    t[532] = "Conversion of interval failed";
    t[533] = "Conversão de interval falhou";
    t[540] = "Query timeout must be a value greater than or equals to 0.";
    t[541] = "Tempo de espera da consulta deve ser um valor maior ou igual a 0.";
    t[542] = "Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.";
    t[543] = "Conexão foi fechada automaticamente porque uma nova conexão foi aberta pelo mesmo PooledConnection ou o PooledConnection foi fechado.";
    t[544] = "ResultSet not positioned properly, perhaps you need to call next.";
    t[545] = "ResultSet não está posicionado corretamente, talvez você precise chamar next.";
    t[546] = "Prepare called before end. prepare xid={0}, state={1}";
    t[547] = "Prepare executado antes do end. prepare xid={0}, state={1}";
    t[548] = "Invalid UUID data.";
    t[549] = "dado UUID é inválido.";
    t[550] = "This statement has been closed.";
    t[551] = "Este comando foi fechado.";
    t[552] = "Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.";
    t[553] = "Não pode inferir um tipo SQL a ser usado para uma instância de {0}. Use setObject() com um valor de Types explícito para especificar o tipo a ser usado.";
    t[554] = "Cannot call updateRow() when on the insert row.";
    t[555] = "Não pode chamar updateRow() quando estiver inserindo registro.";
    t[562] = "Detail: {0}";
    t[563] = "Detalhe: {0}";
    t[566] = "Cannot call deleteRow() when on the insert row.";
    t[567] = "Não pode chamar deleteRow() quando estiver inserindo registro.";
    t[568] = "Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.";
    t[569] = "Posicionado antes do início do ResultSet.  Você não pode chamar deleteRow() aqui.";
    t[576] = "Illegal UTF-8 sequence: final value is a surrogate value: {0}";
    t[577] = "Sequência UTF-8 ilegal: valor final é um valor suplementar: {0}";
    t[578] = "Unknown Response Type {0}.";
    t[579] = "Tipo de Resposta Desconhecido {0}.";
    t[582] = "Unsupported value for stringtype parameter: {0}";
    t[583] = "Valor do parâmetro stringtype não é suportado: {0}";
    t[584] = "Conversion to type {0} failed: {1}.";
    t[585] = "Conversão para tipo {0} falhou: {1}.";
    t[586] = "This SQLXML object has not been initialized, so you cannot retrieve data from it.";
    t[587] = "Este objeto SQLXML não foi inicializado, então você não pode recuperar dados dele.";
    t[600] = "Unable to load the class {0} responsible for the datatype {1}";
    t[601] = "Não foi possível carregar a classe {0} responsável pelo tipo de dado {1}";
    t[604] = "The fastpath function {0} is unknown.";
    t[605] = "A função do fastpath {0} é desconhecida.";
    t[608] = "Malformed function or procedure escape syntax at offset {0}.";
    t[609] = "Sintaxe de escape mal formada da função ou do procedimento no deslocamento {0}.";
    t[612] = "Provided Reader failed.";
    t[613] = "Reader fornecido falhou.";
    t[614] = "Maximum number of rows must be a value grater than or equal to 0.";
    t[615] = "Número máximo de registros deve ser um valor maior ou igual a 0.";
    t[616] = "Failed to create object for: {0}.";
    t[617] = "Falhou ao criar objeto para: {0}.";
    t[620] = "Conversion of money failed.";
    t[621] = "Conversão de money falhou.";
    t[622] = "Premature end of input stream, expected {0} bytes, but only read {1}.";
    t[623] = "Fim de entrada prematuro, eram esperados {0} bytes, mas somente {1} foram lidos.";
    t[626] = "An unexpected result was returned by a query.";
    t[627] = "Um resultado inesperado foi retornado pela consulta.";
    t[644] = "Invalid protocol state requested. Attempted transaction interleaving is not supported. xid={0}, currentXid={1}, state={2}, flags={3}";
    t[645] = "Intercalação de transação não está implementado. xid={0}, currentXid={1}, state={2}, flags={3}";
    t[646] = "An error occurred while setting up the SSL connection.";
    t[647] = "Um erro ocorreu ao estabelecer uma conexão SSL.";
    t[654] = "Illegal UTF-8 sequence: {0} bytes used to encode a {1} byte value: {2}";
    t[655] = "Sequência UTF-8 ilegal: {0} bytes utilizados para codificar um valor de {1} bytes: {2}";
    t[656] = "Not implemented: Prepare must be issued using the same connection that started the transaction. currentXid={0}, prepare xid={1}";
    t[657] = "Não está implementado: Prepare deve ser executado utilizando a mesma conexão que iniciou a transação. currentXid={0}, prepare xid={1}";
    t[658] = "The SSLSocketFactory class provided {0} could not be instantiated.";
    t[659] = "A classe SSLSocketFactory forneceu {0} que não pôde ser instanciado.";
    t[662] = "Failed to convert binary xml data to encoding: {0}.";
    t[663] = "Falhou ao converter dados xml binários para codificação: {0}.";
    t[670] = "Position: {0}";
    t[671] = "Posição: {0}";
    t[676] = "Location: File: {0}, Routine: {1}, Line: {2}";
    t[677] = "Local: Arquivo: {0}, Rotina: {1}, Linha: {2}";
    t[684] = "Cannot tell if path is open or closed: {0}.";
    t[685] = "Não pode dizer se caminho está aberto ou fechado: {0}.";
    t[690] = "Unable to create StAXResult for SQLXML";
    t[691] = "Não foi possível criar StAXResult para SQLXML";
    t[700] = "Cannot convert an instance of {0} to type {1}";
    t[701] = "Não pode converter uma instância de {0} para tipo {1}";
    t[710] = "{0} function takes four and only four argument.";
    t[711] = "função {0} recebe somente quatro argumentos.";
    t[716] = "Error disabling autocommit";
    t[717] = "Erro ao desabilitar autocommit";
    t[718] = "Interrupted while attempting to connect.";
    t[719] = "Interrompido ao tentar se conectar.";
    t[722] = "Your security policy has prevented the connection from being attempted.  You probably need to grant the connect java.net.SocketPermission to the database server host and port that you wish to connect to.";
    t[723] = "Sua política de segurança impediu que a conexão pudesse ser estabelecida. Você provavelmente precisa conceder permissão em java.net.SocketPermission para a máquina e a porta do servidor de banco de dados que você deseja se conectar.";
    t[734] = "No function outputs were registered.";
    t[735] = "Nenhum saída de função foi registrada.";
    t[736] = "{0} function takes one and only one argument.";
    t[737] = "função {0} recebe somente um argumento.";
    t[744] = "This ResultSet is closed.";
    t[745] = "Este ResultSet está fechado.";
    t[746] = "Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.";
    t[747] = "Caracter inválido foi encontrado. Isso é mais comumente causado por dado armazenado que contém caracteres que são inválidos para a codificação que foi criado o banco de dados. O exemplo mais comum disso é armazenar dados de 8 bits em um banco de dados SQL_ASCII.";
    t[752] = "GSS Authentication failed";
    t[753] = "Autenticação GSS falhou";
    t[754] = "Ran out of memory retrieving query results.";
    t[755] = "Memória insuficiente ao recuperar resultados da consulta.";
    t[756] = "Returning autogenerated keys is not supported.";
    t[757] = "Retorno de chaves geradas automaticamente não é suportado.";
    t[760] = "Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.";
    t[761] = "Operação requer um ResultSet rolável, mas este ResultSet é FORWARD_ONLY (somente para frente).";
    t[762] = "A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.";
    t[763] = "Uma função foi executada e o parâmetro de retorno {0} era do tipo {1} contudo tipo {2} foi registrado.";
    t[764] = "Unable to find server array type for provided name {0}.";
    t[765] = "Não foi possível encontrar tipo matriz para nome fornecido {0}.";
    t[768] = "Unknown ResultSet holdability setting: {0}.";
    t[769] = "Definição de durabilidade do ResultSet desconhecida: {0}.";
    t[772] = "Transaction isolation level {0} not supported.";
    t[773] = "Nível de isolamento da transação {0} não é suportado.";
    t[774] = "Zero bytes may not occur in identifiers.";
    t[775] = "Zero bytes não podem ocorrer em identificadores.";
    t[776] = "No results were returned by the query for oid {0}.";
    t[777] = "Nenhum resultado foi retornado pela consulta for oid {0}.";
    t[778] = "A CallableStatement was executed with nothing returned.";
    t[779] = "Uma função foi executada e nada foi retornado.";
    t[780] = "wasNull cannot be call before fetching a result.";
    t[781] = "wasNull não pode ser chamado antes de obter um resultado.";
    t[784] = "Returning autogenerated keys by column index is not supported.";
    t[785] = "Retorno de chaves geradas automaticamente por índice de coluna não é suportado.";
    t[786] = "This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.";
    t[787] = "Este comando não declara um parâmetro de saída. Utilize '{' ?= chamada ... '}' para declarar um)";
    t[788] = "Can''t use relative move methods while on the insert row.";
    t[789] = "Não pode utilizar métodos de movimentação relativos enquanto estiver inserindo registro.";
    t[790] = "A CallableStatement was executed with an invalid number of parameters";
    t[791] = "Uma função foi executada com um número inválido de parâmetros";
    t[792] = "Connection is busy with another transaction";
    t[793] = "Conexão está ocupada com outra transação";
    table = t;
  }
  public java.lang.Object handleGetObject (java.lang.String msgid) throws java.util.MissingResourceException {
    int hash_val = msgid.hashCode() & 0x7fffffff;
    int idx = (hash_val % 397) << 1;
    {
      java.lang.Object found = table[idx];
      if (found == null)
        return null;
      if (msgid.equals(found))
        return table[idx + 1];
    }
    int incr = ((hash_val % 395) + 1) << 1;
    for (;;) {
      idx += incr;
      if (idx >= 794)
        idx -= 794;
      java.lang.Object found = table[idx];
      if (found == null)
        return null;
      if (msgid.equals(found))
        return table[idx + 1];
    }
  }
  public java.util.Enumeration getKeys () {
    return
      new java.util.Enumeration() {
        private int idx = 0;
        { while (idx < 794 && table[idx] == null) idx += 2; }
        public boolean hasMoreElements () {
          return (idx < 794);
        }
        public java.lang.Object nextElement () {
          java.lang.Object key = table[idx];
          do idx += 2; while (idx < 794 && table[idx] == null);
          return key;
        }
      };
  }
  public java.util.ResourceBundle getParent () {
    return parent;
  }
}
