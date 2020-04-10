-- | Main entry point to the application.
module Main where

import Data.Char
import Lab3

-- | The main entry point.
main :: IO ()
main = do
    putStrLn (show (evens [1..10]))
    putStrLn (show (sum . evens $ [827305 .. 927104]))
    putStrLn (show (sum . evens $ []))
-- DNF    
--    putStrLn (show (sum . evens $ [1,3..]))    
    putStrLn (show (squares 10))
    putStrLn (show (sumSquares 50))
    putStrLn (show (squares' 4 2))    
    putStrLn (show (sumSquares' 50)) 
    putStrLn $ show $ sum $ squares' 10 0
    putStrLn $ show $ sum $ squares' 0 10
    
    putStrLn $ show $ coords 1 1
    putStrLn $ show $ coords 1 2
    putStrLn $ show $ coords 5 7        
    putStrLn $ show $ foldr (-) 0 . map (uncurry (*)) $ coords 5 7    
    
