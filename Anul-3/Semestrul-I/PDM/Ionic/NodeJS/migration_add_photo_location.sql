-- Migration script to add photo and location columns to movies table
-- Run this script in your MariaDB/MySQL database

USE pdm;

-- Add photo columns
ALTER TABLE movies 
ADD COLUMN photoPath VARCHAR(500) NULL AFTER owner_id,
ADD COLUMN photoUrl VARCHAR(500) NULL AFTER photoPath;

-- Add location columns
ALTER TABLE movies 
ADD COLUMN latitude DECIMAL(10, 8) NULL AFTER photoUrl,
ADD COLUMN longitude DECIMAL(11, 8) NULL AFTER latitude,
ADD COLUMN locationLabel VARCHAR(255) NULL AFTER longitude;

-- Verify the changes
DESCRIBE movies;








