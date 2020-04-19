SELECT FORMAT(@i:= @i + 1, 0) as rank, name, votes 
    FROM 
        (SELECT @i := 0) as rank,
        votes 
    ORDER BY votes DESC;
