CREATE DATABASE BloggingPlatform;

USE BloggingPlatform;

CREATE TABLE Articles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(100),
    status ENUM('Draft', 'Published') DEFAULT 'Draft',
    scheduled_date DATETIME
);

CREATE TABLE Categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE
);
