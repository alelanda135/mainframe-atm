
-- Crear la tabla de usuarios (modificada para incluir el nombre)
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    pin INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('activo', 'inactivo') NOT NULL DEFAULT 'activo'
);

-- Crear tabla cuentas
CREATE TABLE IF NOT EXISTS cuentas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    numero_cuenta VARCHAR(20) NOT NULL,
    tipo_cuenta ENUM('ahorros', 'corriente') NOT NULL,
    saldo DOUBLE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Crear tabla historico
CREATE TABLE IF NOT EXISTS historico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT,
    numero_cuenta VARCHAR(20) NOT NULL,
    accion ENUM('deposito', 'retiro', 'transferencia') NOT NULL,
    cantidad DOUBLE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Crear tabla transferencias
CREATE TABLE IF NOT EXISTS transferencias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    emisor_id INT,
    receptor_id INT,
    emisor_cuenta VARCHAR(20) NOT NULL,
    receptor_cuenta VARCHAR(20) NOT NULL,
    cantidad DOUBLE NOT NULL,
    FOREIGN KEY (emisor_id) REFERENCES usuarios(id),
    FOREIGN KEY (receptor_id) REFERENCES usuarios(id)
);



-- Insertar datos de ejemplo en usuarios
INSERT INTO usuarios (nombre, pin) VALUES
    ('John Doe', 1234),
    ('Jane Smith', 5678),
    ('Michael Johnson', 9876),
    ('Emily Brown', 4321),
    ('David Wilson', 8765);

-- Insertar datos para cuentas
-- Cuentas de John Doe
INSERT INTO cuentas (usuario_id, numero_cuenta, tipo_cuenta, saldo) VALUES
    (1, '1234567890', 'ahorros', 1000.00);

-- Cuentas de Jane Smith
INSERT INTO cuentas (usuario_id, numero_cuenta, tipo_cuenta, saldo) VALUES
    (2, '5432109876', 'ahorros', 1500.00);

-- Cuentas de Michael Johnson
INSERT INTO cuentas (usuario_id, numero_cuenta, tipo_cuenta, saldo) VALUES
    (3, '2468135790', 'corriente', 2000.00);

-- Cuentas de Emily Brown
INSERT INTO cuentas (usuario_id, numero_cuenta, tipo_cuenta, saldo) VALUES
    (4, '1357924680', 'ahorros', 800.00);

-- Cuentas de David Wilson
INSERT INTO cuentas (usuario_id, numero_cuenta, tipo_cuenta, saldo) VALUES
    (5, '9876540123', 'ahorros', 1200.00);

-- Transferencias ficticias entre cuentas
INSERT INTO transferencias (emisor_id, receptor_id, emisor_cuenta, receptor_cuenta, cantidad) VALUES
    (1, 2, '1234567890', '5432109876', 200.00),  -- John Doe a Jane Smith
    (3, 4, '2468135790', '1357924680', 500.00),  -- Michael Johnson a Emily Brown
    (5, 2, '9876540123', '5432109876', 300.00),  -- David Wilson a Jane Smith
    (2, 3, '5432109876', '2468135790', 100.00),  -- Jane Smith a Michael Johnson
    (4, 1, '1357924680', '1234567890', 50.00);   -- Emily Brown a John Doe



-- Depósitos ficticios en cuentas
INSERT INTO historico (usuario_id, numero_cuenta, accion, cantidad) VALUES
    (1, '1234567890', 'deposito', 300.00),   -- John Doe deposita
    (2, '5432109876', 'deposito', 450.00),   -- Jane Smith deposita
    (3, '2468135790', 'deposito', 750.00),   -- Michael Johnson deposita
    (4, '1357924680', 'deposito', 100.00),   -- Emily Brown deposita
    (5, '9876540123', 'deposito', 200.00);   -- David Wilson deposita


-- Retiros ficticios de cuentas
INSERT INTO historico (usuario_id, numero_cuenta, accion, cantidad) VALUES
    (1, '1234567890', 'retiro', 50.00),    -- John Doe retira
    (2, '5432109876', 'retiro', 75.00),    -- Jane Smith retira
    (3, '2468135790', 'retiro', 100.00),   -- Michael Johnson retira
    (4, '1357924680', 'retiro', 30.00),    -- Emily Brown retira
    (5, '9876540123', 'retiro', 80.00);    -- David Wilson retira



