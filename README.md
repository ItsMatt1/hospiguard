# hospiguard

Nome do Projeto: HospiGuard - Sistema Ubíquo de Monitoramento Hospitalar

Descrição:
O HospiGuard é um projeto que visa aprimorar a segurança e o atendimento a pacientes em ambientes hospitalares por meio de um sistema ubíquo de monitoramento. Utilizando uma variedade de sensores e dispositivos inteligentes, o HospiGuard oferece uma solução completa para monitorar quartos de hospitais, garantindo o bem-estar dos pacientes, melhorando a eficiência do atendimento e proporcionando paz de espírito tanto para pacientes quanto para profissionais de saúde.

Digital Twin dos Quartos de Hospital:
Crie uma réplica digital de cada quarto de hospital monitorado pelo sistema HospiGuard. Isso permitirá uma visualização em tempo real do estado de cada quarto, com informações detalhadas sobre temperatura, umidade, presença de pacientes, níveis de gases, entre outros.
Digital Twin dos Sensores:
Desenvolva uma representação digital dos sensores utilizados no sistema. Isso pode facilitar a monitorização contínua do desempenho dos sensores, identificando eventuais falhas ou calibrações necessárias.

Recursos Destacados:

    Detecção de Movimento: Sensores de movimento identificam a presença de pessoas no quarto, permitindo uma resposta rápida às necessidades dos pacientes.
    Monitoramento de Temperatura e Umidade: Garanta o conforto dos pacientes, evitando problemas de saúde relacionados ao ambiente.
    Detecção de Gases Perigosos: Proteja contra incêndios e vazamentos de gases potencialmente perigosos.
    Monitoramento de Som: Capture alarmes e chamados de socorro dos pacientes.
    Ajuste Automático da Iluminação: Sensores de luz ajustam a iluminação do quarto para maior conforto.
    Privacidade e Segurança: Respeitamos a privacidade dos pacientes e cumprimos todas as regulamentações de proteção de dados.
    Integração com Sistemas de Registro Eletrônico de Saúde (EHR): Forneça informações em tempo real para profissionais de saúde.
    Acessibilidade: Projetado para ser acessível a uma ampla variedade de dispositivos e sistemas.

Caso de Uso: HC01 - Monitoramento do Paciente

Funcionalidade: HC01a - Detecção de Quedas

Cenário: HC01a-C01 - Detecção de Queda do Paciente

Dado que o sistema HospiGuard está operacional no quarto do paciente

Quando o paciente cai no chão

Então o sistema detecta a queda por meio de sensores de movimento

E envia um alerta imediato para a equipe de enfermagem

E exibe uma notificação no painel de controle do enfermeiro

Cenário: HC01a-C02 - Falso Alarme de Queda

Dado que o sistema HospiGuard está operacional no quarto do paciente

Quando ocorre um evento que pode ser confundido com uma queda, como um movimento brusco do paciente na cama

Então o sistema registra o evento, mas não aciona um alerta

E o sistema diferencia entre quedas reais e outros movimentos

Funcionalidade: HC01b - Monitoramento de Sinais Vitais

Cenário: HC01b-C01 - Monitoramento Contínuo da Pressão Arterial e Batimentos Cardíacos

Dado que o sistema HospiGuard está operacional no quarto do paciente

Quando o paciente está deitado na cama

Então o sistema monitora continuamente a pressão arterial e os batimentos cardíacos por meio de sensores não invasivos

E registra as leituras em intervalos regulares

Cenário: HC01b-C02 - Alerta de Leituras Anormais

Dado que o sistema HospiGuard está operacional no quarto do paciente

Quando as leituras de pressão arterial ou batimentos cardíacos saem dos limites normais

Então o sistema emite um alerta para a equipe médica

E exibe informações detalhadas das leituras anormais no painel de controle do enfermeiro

Caso de Uso: HC02 - Monitoramento Ambiental

Funcionalidade: HC02a - Monitoramento de Temperatura e Umidade

Cenário: HC02a-C01 - Monitoramento de Condições Ambientais

Dado que o sistema HospiGuard está operacional no quarto do paciente

Quando as condições de temperatura ou umidade atingem níveis críticos ou desconfortáveis

Então o sistema registra as condições ambientais e aciona um alerta para a equipe de manutenção hospitalar

E exibe informações sobre a situação no painel de controle do responsável pela manutenção

Funcionalidade: HC02b - Detecção de Gases Perigosos

Cenário: HC02b-C01 - Detecção de Gases Perigosos

Dado que o sistema HospiGuard está operacional no quarto do paciente

Quando são detectados níveis perigosos de gases como monóxido de carbono ou fumaça

Então o sistema aciona um alarme audível e visual no quarto do paciente

E envia um alerta para a equipe de segurança do hospital

E exibe informações detalhadas sobre a detecção no painel de controle da equipe de segurança

Caso de Uso: HC03 - Privacidade e Segurança do Paciente

Funcionalidade: HC03a - Controle de Acesso aos Dados do Paciente

Cenário: HC03a-C01 - Acesso Autorizado aos Dados do Paciente

Dado que o sistema HospiGuard está operacional no quarto do paciente

Quando um profissional de saúde autorizado acessa o sistema para visualizar os dados do paciente

Então o sistema concede acesso aos dados relevantes, respeitando a privacidade e segurança do paciente

Cenário: HC03a-C02 - Tentativa de Acesso Não Autorizado aos Dados do Paciente

Dado que o sistema HospiGuard está operacional no quarto do paciente

Quando uma tentativa não autorizada de acessar os dados do paciente ocorre

Então o sistema registra a tentativa e notifica o responsável pela segurança do hospital

E impede o acesso aos dados confidenciais do paciente
