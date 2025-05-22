SELECT * FROM MenuItems;

SELECT * FROM AddOns;

SELECT * FROM Orders;


SELECT * FROM OrderDetails;


SELECT * FROM OrderAddOns;

SELECT * FROM Sales;

SELECT * FROM Users;

SELECT COUNT(*) AS TotalTransactions FROM Sales;

Sales
SELECT 
    MIN(TotalPrice) AS MinSale, 
    MAX(TotalPrice) AS MaxSale, 
    AVG(TotalPrice) AS AvgSale, 
    SUM(TotalPrice) AS TotalRevenue 
FROM Sales;

SELECT 
    m.Name, 
    SUM(s.Quantity) AS TotalQty, 
    SUM(s.TotalPrice) AS TotalSales
FROM Sales s
JOIN MenuItems m ON s.MenuItemID = m.MenuItemID
GROUP BY m.Name;

SELECT 
    m.Category, 
    COUNT(*) AS ItemsSold, 
    SUM(s.TotalPrice) AS Revenue
FROM Sales s
JOIN MenuItems m ON s.MenuItemID = m.MenuItemID
GROUP BY m.Category;

SELECT 
    o.OrderID, 
    o.OrderDate, 
    m.Name, 
    d.Quantity, 
    m.BasePrice,
    (d.Quantity * m.BasePrice) AS Subtotal
FROM Orders o
JOIN OrderDetails d ON o.OrderID = d.OrderID
JOIN MenuItems m ON d.MenuItemID = m.MenuItemID;

SELECT 
    od.OrderDetailID, 
    ao.Name AS AddOnName, 
    ao.Price
FROM OrderAddOns oa
JOIN AddOns ao ON oa.AddOnID = ao.AddOnID
JOIN OrderDetails od ON oa.OrderDetailID = od.OrderDetailID;

CREATE OR REPLACE VIEW SalesSummary AS
SELECT 
    s.id AS SaleID,
    m.Name AS ItemName,
    m.Category,
    s.Quantity,
    s.TotalPrice,
    DATE(s.date_time) AS SaleDate,
    TIME(s.date_time) AS SaleTime
FROM Sales s
JOIN MenuItems m ON s.MenuItemID = m.MenuItemID;

SELECT * FROM SalesSummary;
