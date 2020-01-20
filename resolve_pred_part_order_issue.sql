select part_type, predicate_part_order from criteria_predicate_part where part_id in (
    select source_part_id from condition_crit_pred_part where predicate_id =
        (select predicate_id from condition_crit_pred_part where part_id = 'f1fc4fdfed7c0656638cf1e3dea5c0ba')
)

