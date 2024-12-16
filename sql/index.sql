USE baza;

CREATE INDEX idx_user_id ON RentalRegister(user_id);
CREATE INDEX idx_date_range ON RentalRegister(start_date, end_date);
CREATE INDEX idx_equipment_id ON RentalRegister(equipment_id);

CREATE INDEX idx_status ON Equipment(status_id);
CREATE INDEX idx_category ON Equipment(category_id);

CREATE INDEX idx_equipment_id ON RentalEquipment(equipment_id);
