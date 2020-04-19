SELECT 
    id,
    @name := substring_index(reverse(substring_index(reverse(name),'|',idx)),'|', 1) AS name 
FROM 
    sometbl, 
    (SELECT @row := @row + 1 AS idx FROM
	    # up to one hundred substrings, add more subselects if higher orders need to be supported
	    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
	    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
	    (SELECT @row:=0) init  
    ) series 
WHERE
    # To avoid duplicates - another alternative would be to use distinct
    substring_index(name,'|', idx-1) <> substring_index(name,'|', idx) 
ORDER BY 
    id,-idx