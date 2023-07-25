# Estacionamento Deep - Aplicação de Gerenciamento 🚗💰 

## Descrição
A aplicação foi desenvolvida utilizando a linguagem de programação Java e utiliza um banco de dados para armazenar e gerenciar as informações. O Estacionamento Deep precisa de uma aplicação para gerenciar a entrada, permanência e saída dos carros no estacionamento. A aplicação permitirá ao cliente visualizar os carros presentes no estacionamento, sua permanência, valor a ser pago e o nome do dono do carro.  <br>
 <br>
 Cada permanência terá um valor associado: <br>
Até 1 hora: R$10 <br>
Adicional de 30 minutos: R$2 <br>
Meio período (12 horas): R$90 <br>

## Funcionalidades
A aplicação contém as seguintes funcionalidades:<br>

🚗  __Cadastrar Novo Carro__ <br>
O cliente poderá cadastrar um novo carro no estacionamento, informando o nome do carro, sua placa, a data e hora de entrada, bem como o nome do dono do carro.<br>
A aplicação calculará automaticamente o valor a ser pago com base no período de permanência do carro.<br>

🔍 __Visualizar Histórico de Permanência__ <br>
O cliente poderá visualizar todas as informações registradas dos carros estacionados, incluindo o nome do cliente, CPF, nome do carro, placa, data e hora de entrada, data e hora de saída (se disponível) e o valor pago (se aplicável).

🔄  __Atualizar Data/Hora de Saída__ <br>
O cliente poderá atualizar a data e hora de saída de um carro que ainda está no estacionamento. Isso permite corrigir ou ajustar a permanência do carro.

🔄  __Atualizar Data/Hora de Entrada__ <br>
O cliente poderá atualizar a data e hora de entrada de um carro já registrado. Isso é útil para corrigir possíveis erros no momento do registro.

💰 __Pagamento__ <br>
O cliente poderá calcular o valor a ser pago por um determinado carro com base em sua permanência atual. O valor é calculado com base no tempo de permanência e na tabela de preços mencionada acima.
Após calcular o valor, o cliente poderá marcar que o pagamento foi realizado, o que resultará na exclusão do registro do carro do banco de dados.

## Arquitetura do Banco de Dados
A aplicação utiliza um banco de dados compartilhado para armazenar as informações dos clientes, carros estacionados e suas permanências. A arquitetura do banco de dados é composta por três tabelas:

🧑‍💼 __clientes:__ Armazena informações dos clientes, incluindo ID, nome e CPF.

🚗 __carros:__ Armazena informações dos carros estacionados, incluindo ID, ID do cliente associado, nome do carro, placa, data e hora de entrada.

⏱️ __permanencias:__ Armazena informações das permanências dos carros, incluindo ID do carro associado, data e hora de saída e valor total a ser pago.
