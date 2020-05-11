select predicate_id, predicate_part_order, count(*)
from criteria_predicate_part
group by predicate_id, predicate_part_order
having count(*) > 1;

select predicate_id, part_id, part_type, predicate_part_order
from criteria_predicate_part
where predicate_id = 'bdaa0aea849f1640f1b9add057c31d6c'
order by predicate_part_order;

select predicate_id, part_id, part_type, predicate_part_order
from criteria_predicate_part
where predicate_id = 'b5d6c90e172b67a44b344067f2bffe8d'
order by predicate_part_order;

select predicate_id, part_id, part_type, predicate_part_order
from criteria_predicate_part
where predicate_id = '684f7580d5c795b182d7c0e9d5c25595'
order by predicate_part_order;