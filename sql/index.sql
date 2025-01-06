USE baza;

CREATE INDEX idx_user_id ON rental_register(user_id);
CREATE INDEX idx_date_range ON rental_register(start_date, end_date);
CREATE INDEX idx_equipment_id ON rental_register(equipment_id);

CREATE INDEX idx_status ON equipment(status_id);
CREATE INDEX idx_category ON equipment(category_id);

CREATE INDEX idx_equipment_id ON rental_equipment(equipment_id);
