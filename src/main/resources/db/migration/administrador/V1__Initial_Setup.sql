create table public.empresa (id_empresa int4 not null, cnpj varchar(255), nome varchar(255), primary key (id_empresa));
create table public.papel (id_papel int4 not null, codigo varchar(500), descricao varchar(500), primary key (id_papel));
create table public.permissao (id_usuario_empresa int4 not null, id_papel int4 not null);
create table public.pessoa (id_pessoa int4 not null, ativo boolean default true, celular varchar(25), cpf varchar(255), data_nascimento DATE, email varchar(255), nome varchar(180) not null, sexo char(1) default 'I', primary key (id_pessoa));
create table public.usuario (id_usuario int4 not null, ativo boolean default true, login varchar(255) not null, senha varchar(255), solicitacao_acesso boolean default false, id_pessoa int4, primary key (id_usuario));
create table public.usuario_empresa (id_usuario_empresa int4 not null, id_empresa int4, id_usuario int4, primary key (id_usuario_empresa));

alter table public.usuario add constraint UK_pm3f4m4fqv89oeeeac4tbe2f4  unique (login);
alter table public.permissao add constraint FK_v7tw2q0wghuovqk4xfhlxco4 foreign key (id_papel) references public.papel;
alter table public.permissao add constraint FK_lhca6uo9e9tdu3g2yxcfbn8ab foreign key (id_usuario_empresa) references public.usuario_empresa;
alter table public.usuario add constraint FK_9wnw55sajbeqbpd8fsjbna1ie foreign key (id_pessoa) references public.pessoa;
alter table public.usuario_empresa add constraint FK_5ggb71ly76np781jcs2n05clq foreign key (id_empresa) references public.empresa;
alter table public.usuario_empresa add constraint FK_bkqyakhs51qesf38ntlbqrxyd foreign key (id_usuario) references public.usuario;

create sequence public.id_seq_empresa;
create sequence public.id_seq_papel;
create sequence public.id_seq_pessoa;
create sequence public.id_seq_produto;
create sequence public.id_seq_usuario;
create sequence public.id_seq_usuario_empresa;