-- Выбрать первые 10 поставщиков по количеству поставленного товара
select o.* from organisation as o
inner join invoice as i on o.tin = i.organisation_tin
	inner join invoice_item as item on i.id = item.invoice_id
group by o.tin order by sum(item.amount) desc limit 10

-- Выбрать поставщиков с количеством поставленного товара выше указанного значения (товар и его количество должны допускать множественное указание).
select o.* from organisation as o
inner join invoice as i on i.organisation_tin = o.tin
inner join invoice_item as item on i.id = item.invoice_id
group by o.tin having sum(item.amount) > 12;

select o.*, sum(item.amount) from organisation as o
inner join invoice as i on i.organisation_tin = o.tin
inner join invoice_item as item on i.id = item.invoice_id and item.product_id = 7
group by o.tin having sum(item.amount) > 5;


-- За каждый день для каждого товара рассчитать количество и сумму полученного товара в указанном периоде, посчитать итоги за период
select i.invoice_date, item.product_id, sum(item.amount) as amount, sum(item.cost) as cost, sum(item.amount * item.cost) as total from invoice_item as item
inner join invoice as i on item.invoice_id = i.id
where i.invoice_date between '2021-04-04' and '2022-03-04'
group by i.invoice_date, item.product_id order by i.invoice_date;

-- Рассчитать среднюю цену по каждому товару за период
select item.product_id, avg(cost) as average_cost from invoice_item as item
inner join invoice as i on item.invoice_id = i.id
where i.invoice_date between '2021-04-04' and '2021-05-24'
group by item.product_id;


-- Вывести список товаров, поставленных организациями за период. Если организация товары не поставляла, то она все равно должна быть отражена в списке.
select product_id, o.tin from organisation as o
inner join invoice as i on i.organisation_tin = o.tin
	inner join invoice_item as item on item.invoice_id = i.id
		where i.invoice_date between '2021-04-04' and '2021-05-24'
union select NULL, o.tin from organisation as o
where not exists (
	select * from invoice as i2
		where i2.organisation_tin = o.tin and
			i2.invoice_date between '2021-04-04' and '2021-05-24'
);
