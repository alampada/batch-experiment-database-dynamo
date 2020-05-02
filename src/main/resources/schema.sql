drop table if exists ordercontents;

drop table if exists orders;

create table orders (
    id bigint not null,
    primary key (id)
);

create table ordercontents (
    orderId bigint not null,
    productId bigint not null,
    primary key (orderId, productId),
    foreign key (orderId) references orders(id)
);
