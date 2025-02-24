-- 1) Create a new database (schema)
CREATE DATABASE IF NOT EXISTS my_navigation_db;

-- 2) Switch to that database
USE my_navigation_db;

-- Clean up old tables if they exist (optional)
DROP TABLE IF EXISTS roads;
DROP TABLE IF EXISTS stations;

-- 3) Create the 'stations' table
CREATE TABLE IF NOT EXISTS stations (
    station_id INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    x_coord    DOUBLE NOT NULL,
    y_coord    DOUBLE NOT NULL
);

-- 4) Create the 'roads' table with an extra column 'bus_color'
CREATE TABLE IF NOT EXISTS roads (
    road_id         INT AUTO_INCREMENT PRIMARY KEY,
    from_station_id INT NOT NULL,
    to_station_id   INT NOT NULL,
    mode            ENUM('CAR','BUS') NOT NULL,
    is_one_way      BOOLEAN NOT NULL,
    bus_color       VARCHAR(20) DEFAULT NULL,
    FOREIGN KEY (from_station_id) REFERENCES stations(station_id),
    FOREIGN KEY (to_station_id)   REFERENCES stations(station_id)
);

-- Stations
INSERT INTO stations (name, x_coord, y_coord) VALUES
('Station A', -4,  2),  -- (ID=1)
('Station B', -2,  4),  -- (ID=2)
('Station C',  2,  4),  -- (ID=3)
('Station D',  0,  2),  -- (ID=4)
('Station E',  2, -2),  -- (ID=5)
('Station F', -2, -6),  -- (ID=6)
('Station G', -8, -4);  -- (ID=7)

-- CAR
INSERT INTO roads (from_station_id, to_station_id, mode, is_one_way, bus_color)
VALUES
(1, 2, 'CAR', TRUE,  NULL),  -- A->B
(1, 4, 'CAR', TRUE,  NULL),  -- A->D
(2, 3, 'CAR', TRUE,  NULL),  -- B->C
(3, 4, 'CAR', TRUE,  NULL),  -- C->D
(3, 5, 'CAR', TRUE, NULL),  -- C->E
(4, 6, 'CAR', TRUE,  NULL),  -- D->F
(4, 7, 'CAR', FALSE,  NULL),  -- D->G
(7, 4, 'CAR', FALSE,  NULL),  -- G->D
(5, 6, 'CAR', FALSE,  NULL),  -- E->F
(6, 5, 'CAR', FALSE,  NULL),  -- F->E
(6, 7, 'CAR', TRUE,  NULL),  -- F->G
(7, 1, 'CAR', TRUE,  NULL);  -- G->A

-- BUS 1 (RED)
INSERT INTO roads (from_station_id, to_station_id, mode, is_one_way, bus_color)
VALUES
(1, 2, 'BUS', TRUE, 'RED'),   -- A->B
(2, 3, 'BUS', TRUE, 'RED'),   -- B->C
(3, 5, 'BUS', TRUE, 'RED');   -- C->E

-- BUS 2 (GREEN)
INSERT INTO roads (from_station_id, to_station_id, mode, is_one_way, bus_color)
VALUES
(1, 4, 'BUS', TRUE, 'GREEN'), -- A->D
(4, 7, 'BUS', FALSE, 'GREEN'), -- D->G
(7, 1, 'BUS', TRUE, 'GREEN'); -- G->A

-- BUS 3 (YELLOW)
INSERT INTO roads (from_station_id, to_station_id, mode, is_one_way, bus_color)
VALUES
(5, 6, 'BUS', FALSE, 'YELLOW'), -- E->F
(6, 7, 'BUS', TRUE, 'YELLOW'), -- F->G
(7, 4, 'BUS', FALSE, 'YELLOW'); -- G->D


-- BUS 4 (BLUE)
INSERT INTO roads (from_station_id, to_station_id, mode, is_one_way, bus_color)
VALUES
(3, 4, 'BUS', TRUE, 'BLUE'),   -- C->D
(4, 6, 'BUS', TRUE, 'BLUE'),   -- D->F
(6, 5, 'BUS', FALSE, 'BLUE');   -- F->E



