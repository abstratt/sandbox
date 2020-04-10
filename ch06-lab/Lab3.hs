module Lab3 where

-----------------------------------------------------------------------------------------------------------------------------
-- LIST COMPREHENSIONS
------------------------------------------------------------------------------------------------------------------------------

-- ===================================
-- Ex. 0 - 2
-- ===================================

evens :: [Integer] -> [Integer]
--evens = undefined

-- ===================================
-- Ex. 3 - 4 
-- ===================================

-- complete the following line with the correct type signature for this function
-- squares :: ... 
--squares n = undefined

sumSquares :: Integer -> Integer
sumSquares n = sum (squares n)

-- ===================================
-- Ex. 5 - 7
-- ===================================
 
-- complete the following line with the correct type signature for this function
-- squares' :: ...
--squares' m n = undefined

sumSquares' :: Integer -> Integer
sumSquares' x = sum . uncurry squares' $ (x, x)

-- ===================================
-- Ex. 8
-- ===================================

coords :: Integer -> Integer -> [(Integer,Integer)]
--coords = undefined


------ Actual work authored for this lab

evens ns = [x | x <- ns, (mod x 2) == 0]

squares n = [x^2 | x <- [1..n]]

squares' m n = [x^2 | x <- [n+1 .. n+m]]

coords m n = [(x,y) | x <- [0 .. m], y <- [0 .. n]]
