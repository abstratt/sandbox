-- | Main entry point to the application.
module Main where

import Data.Char
import Lab2

-- | The main entry point.
main :: IO ()
main = do
--    putStrLn (show (toDigits(100) == [1,0,0]))
--    putStrLn (show (toDigits(105465460)))
--    putStrLn (show (toDigitsRev 105465460))
--    putStrLn (show (doubleSecond (toDigits 105465460)))
--    putStrLn (show (doubleSecond (toDigits 1)))
--    putStrLn (show (doubleSecond (toDigits 12)))
--    putStrLn (show (doubleSecond (toDigits 123)))
--    putStrLn (show (doubleSecond (toDigits 1234)))
    putStrLn (show (sumDigits [8,14,6,10]))
--    putStrLn (show (toDigits(-1) == [1,0,0]))
    putStrLn (show (isValid 4012888888881881))
    putStrLn ("#0 - " ++ show (toDigits 12321))  
    putStrLn ("#3 - " ++ show (toDigits 0))      
    putStrLn ("#3 - " ++ show (toDigitsRev (12345678)))      
  
    putStrLn ("#5 - " ++ show (toDigits 666))          
    putStrLn ("#6 - " ++ show (toDigitsRev 12321))      
    putStrLn ("#9 - " ++ show (toDigitsRev 0))          
    putStrLn ("#12 - " ++ show (doubleSecond [1,0,1,0,1]))              
    putStrLn ("#19 - " ++ show (sumDigits []))                  
    putStrLn ("#20 - " ++ show (sumDigits [0,0,0]))                  
    putStrLn ("#21 - " ++ show (sumDigits [6,66,666]))                      
    putStrLn ("#24 - " ++ show (isValid (5256283618614517)))                          
    putStrLn ("#25 - " ++ show (isValid (4556945538735694)))                              
    putStrLn ("#26 - " ++ show (isValid (0000000000000000)))                                  
    putStrLn ("#27 - " ++ show (numValid creditcards))                                      
    
--    putStrLn (show ([(cc, isValid cc) | cc <- creditcards]))
    
