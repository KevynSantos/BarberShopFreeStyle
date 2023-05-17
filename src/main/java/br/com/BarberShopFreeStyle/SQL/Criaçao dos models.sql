drop database if exists barbershopfreestyle;

create database barbershopfreestyle;

use barbershopfreestyle;

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(30) DEFAULT NULL,
  `senha` varchar(10) DEFAULT NULL,
  `id_tipo_perfil` int(11) DEFAULT NULL,
  `id_funcionario` int(11) DEFAULT NULL,
  `id_cliente` int(11) DEFAULT NULL,
  `data_exclusao` datetime DEFAULT NULL,
  PRIMARY KEY (`id_usuario`)
);

CREATE TABLE `servico` (
  `id_servico` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) DEFAULT NULL,
  `descricao_servico` varchar(100) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `id_usuario` int(11) DEFAULT NULL,
  `id_agendamento` int(11) DEFAULT NULL,
  `data_criacao` datetime DEFAULT NULL,
  `data_finalizacao` datetime DEFAULT NULL,
  PRIMARY KEY (`id_servico`)
);

CREATE TABLE `tipo_perfil` (
  `id_tipo_perfil` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id_tipo_perfil`)
);

CREATE TABLE `agendamento` (
  `id_agendamento` int(11) NOT NULL AUTO_INCREMENT,
  `hora` time DEFAULT NULL,
  `data` date DEFAULT NULL,
  `checkin` datetime DEFAULT NULL,
  `notificacao` tinyint(1) NOT NULL DEFAULT '0',
  `dt_abertura` datetime,
  PRIMARY KEY (`id_agendamento`)
);

CREATE TABLE `funcionario` (
  `id_funcionario` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) DEFAULT NULL,
  `endereco` varchar(50) DEFAULT NULL,
  `marca_hora` tinyint(1) DEFAULT NULL,
  `cpf` varchar(14) DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  `telefone` varchar(15) DEFAULT NULL,
  `data_nascimento` date DEFAULT NULL,
  PRIMARY KEY (`id_funcionario`)
);

CREATE TABLE `cliente` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) DEFAULT NULL,
  `endereco` varchar(50) DEFAULT NULL,
  `cpf` varchar(14) DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  `telefone` varchar(15) DEFAULT NULL,
  `data_nascimento` date DEFAULT NULL,
  `data_exclusao` datetime DEFAULT NULL,
  PRIMARY KEY (`id_cliente`)
);

CREATE TABLE `pedido` (
  `id_pedido` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) DEFAULT NULL,
  `tempo` time DEFAULT NULL,
  `data_exclusao` datetime DEFAULT NULL,
  `preco` varchar(300) DEFAULT NULL,
  `produto` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_pedido`)
);

CREATE TABLE `pedido_servico` (
  `id_pedido_servico` int(11) NOT NULL AUTO_INCREMENT,
  `id_servico` int(11) DEFAULT NULL,
  `id_pedido` int(11) DEFAULT NULL,
  `data_exclusao` datetime DEFAULT NULL,
  PRIMARY KEY (`id_pedido_servico`)
);

CREATE TABLE `desconto`(
	`id_desconto` int(11) NOT NULL AUTO_INCREMENT,
    `valor` varchar(10),
    `ativo` tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id_desconto`)
);

CREATE TABLE `pedido_tipo_perfil`(
`id_pedido_tipo_perfil` int(11) NOT NULL AUTO_INCREMENT,
`id_pedido` int(11) NOT NULL,
`id_tipo_perfil` int(11) NOT NULL,
PRIMARY KEY (`id_pedido_tipo_perfil`) 
);

ALTER TABLE usuario
ADD FOREIGN KEY (id_tipo_perfil) REFERENCES tipo_perfil(id_tipo_perfil);

ALTER TABLE usuario
ADD FOREIGN KEY (id_funcionario) REFERENCES funcionario(id_funcionario);

ALTER TABLE servico
ADD FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente);

ALTER TABLE servico
ADD FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario);

ALTER TABLE servico
ADD FOREIGN KEY (id_agendamento) REFERENCES agendamento(id_agendamento);

ALTER TABLE pedido_servico
ADD FOREIGN KEY (id_servico) REFERENCES servico(id_servico);

ALTER TABLE pedido_servico
ADD FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido);

ALTER TABLE pedido_tipo_perfil
ADD FOREIGN KEY (id_tipo_perfil) REFERENCES tipo_perfil(id_tipo_perfil);

-- Dados de teste

-- Cliente

INSERT INTO `cliente` VALUES (null,'Filipe Danilo Vieira','Travessa Onze','891.673.197-69','filipedanilovieira@sicredi.com.br','(93) 2844-2676','1962-09-04',null),(null,'Simone Alessandra da Cruz','5ª Travessa João Salomé','420.971.456-98','ssimonealessandradacruz@systrix.com.br','(71) 99255-5931','1982-04-02',null),(null,'Laís Isabelly Moura','Rua Estado de Goiás','391.049.696-26','llaisisabellymoura@novaequipem.com.br','(91) 99982-5136','1968-06-17',null),(null,'Denison Mota','Rua Belém','155.760.628-50','denisonmota666@gmail.com','(21) 98766-9286','1999-07-10',null);

-- Funcionarios

INSERT INTO `funcionario` VALUES (1,'Marcelo de Melo Ferreira Lima',NULL,1,'607.580.820-53','marcelo@rhyta.com','(21) 98426-2900','1992-09-02'),(2,'Cláudio Thales Yuri da Luz',NULL,0,'246.543.430-67','claudio@dayrep.com','(21) 98238-6485','1989-04-18'),(3,'Eduarda Marcela Pereira',NULL,0,'002.489.870-89','Eduarda@armyspy.com','(21) 99614-8158','1976-01-24'),(4,'Sueli Louise Moraes',NULL,1,'324.729.490-22','Sueli@dayrep.com','(21) 98442-3415','2002-06-12'),(5,'Iago João Emanuel Viana',NULL,1,'488.731.470-11','Iago@teleworm.us','(21) 99547-6775','2001-12-11');

-- Pedidos

INSERT INTO `pedido` VALUES 
(1,'Maquina','00:30:00',null,'R$15,00',0),
(2,'Tesoura','00:50:00',null,'R$25,00',0),
(3,'Barba','00:15:00',null,'R$15,00',0),
(4,'Sobrancelha','00:10:00',null,'R$30,00',0),
(5,'Tintura','01:00:00',null,'R$60,00',0),
(6,'Mãos','01:00:00',null,'R$40,00',0),
(7,'Pés','01:00:00',null,'R$40,00',0),
(8,'Esmalte','00:30:00',null,'R$50,00',0),
(9,'Limpeza','00:30:00',null,'R$90,00',0),
(10,'Lavagem','00:15:00',null,'R$20,00',0),
(11,'Escova','00:15:00',null,'R$20,00',0),
(12,'Mechas/luzes','00:30:00',null,'R$120,00',0),
(13,'Relaxamento','00:25:00',null,'R$80,00',0),
(14,'Progressiva','00:40:00',null,'R$160,00',0),
(15,'Cauterização','00:30:00',null,'R$100,00',0),
(16,'Hidratação','00:30:00',null,'R$80,00',0),
(17,'Botox Capilar','01:00:00',null,'R$600,00',0),
(18,'Penteado','00:20:00',null,'R$25,00',0),
(19,'Maquiagem','00:25:00',null,'R$20,00',0),
(20,'Alongamento Cílios','00:20:00',null,'R$15,00',0),
(21,'Alongamento Unhas','00:20:00',null,'R$15,00',0),
(22,'Depilação','00:30:00',null,'R$20,00',0),
(23,'Axila/Buço','00:10:00',null,'R$10,00',0),
(24,'Pigmentação','00:10:00',null,'R$5,00',0),
(25,'Gel',null,null,'R$7,00',1),
(26,'Shampoo',null,null,'R$15,00',1),
(27,'Blend Original',null,null,'R$100,00',1),
(28,'Aparador de Pelos',null,null,'R$120,00',1),
(29,'Pente Curvo para barba',null,null,'R$32,00',1),
(30,'Minoxidil 1 unid',null,null,'R$48,00',1),
(31,'Esmalte Risqué',null,null,'R$3,00',1),
(32,'Estojo de maquiagem',null,null,'R$130,00',1);


-- Tipo Perfil

INSERT INTO `tipo_perfil` VALUES (1,'ADMINISTRADOR'),(2,'BARBEIRO'),(3,'PEDICURE'),(4,'CABELEIREIRO'),(5,'MANICURE'),(6,'CLIENTE');

-- Usuário

INSERT INTO `usuario` VALUES (1,'marcelo','123',1,1,null,null),(2,'Claudio','123',2,2,null,null),(3,'Eduarda','123',3,3,null,null),(4,'Sueli','123',4,4,null,null),(5,'Iago','123',5,5,null,null);

INSERT INTO `desconto` VALUES(1,'5',0);

-- FUNCIONÁRIOS RESPONSÁVEIS PELOS PEDIDOS

-- ADMINISTRADOR
INSERT INTO `pedido_tipo_perfil` VALUES(null,1,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,2,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,3,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,4,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,5,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,6,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,7,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,8,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,9,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,18,1);
INSERT INTO `pedido_tipo_perfil` VALUES(null,24,1);

-- BARBEIRO
INSERT INTO `pedido_tipo_perfil` VALUES(null,1,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,2,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,3,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,4,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,5,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,16,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,18,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,24,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,25,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,26,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,27,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,28,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,29,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,30,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,31,2);
INSERT INTO `pedido_tipo_perfil` VALUES(null,32,2);

-- CABELEIREIRO
INSERT INTO `pedido_tipo_perfil` VALUES(null,1,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,2,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,3,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,4,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,5,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,10,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,11,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,12,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,13,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,14,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,15,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,16,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,17,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,18,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,23,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,24,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,25,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,26,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,27,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,28,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,29,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,30,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,31,4);
INSERT INTO `pedido_tipo_perfil` VALUES(null,32,4);


-- MANICURE
INSERT INTO `pedido_tipo_perfil` VALUES(null,6,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,8,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,9,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,10,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,13,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,19,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,20,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,21,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,22,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,23,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,25,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,26,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,27,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,28,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,29,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,30,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,31,5);
INSERT INTO `pedido_tipo_perfil` VALUES(null,32,5);

-- PEDICURE
INSERT INTO `pedido_tipo_perfil` VALUES(null,7,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,8,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,9,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,10,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,13,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,19,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,20,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,22,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,23,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,25,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,26,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,27,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,28,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,29,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,30,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,31,3);
INSERT INTO `pedido_tipo_perfil` VALUES(null,32,3);

create table confirma_email
(
	id_confirma_email int primary key not null auto_increment,
    email varchar(50),
    codigo varchar(10),
    data_confirmacao datetime,
    data datetime,
    id_usuario int,
    id_cliente int
)
