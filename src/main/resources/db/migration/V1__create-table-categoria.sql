create table categoria(
    id bigint not null auto_increment,
    nome varchar(50) not null unique,
    descricao varchar(200),
    constraint categoriapk primary key(id)
);
