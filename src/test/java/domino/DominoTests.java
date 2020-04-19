package domino;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/*
"Domino": We are given an S string, representing a domino tile chain. Each tile has an L-R format, where L and R are numbers from the 1..6 range. The tiles are separated by a comma. Some examples of a valid S chain are:

1. "6-3"
2. "4-3,5-1,2-2,1-3,4-4"
3. "1-1,3-5,5-2,2-3,2-4"

Devise a function that given an S string, returns the number of tiles in the longest matching group within S. A matching group is a set of tiles that match and that are subsequent in S. Domino tiles match, if the right side of a tile is the same as the left side of the following tile. "2-4,4-1" are matching tiles, but "5-2,1-6" are not.

domino("1-1,3-5,5-2,2-3,2-4") should return 3.
 */
public class DominoTests {
    @Test
    public void domino() throws Exception {
        Assertions.assertEquals(3, new Domino().solveDomino("1-1,3-5,5-2,2-3,2-4"));
        Assertions.assertEquals(6, new Domino().solveDomino("1-1,3-5,5-2,2-3,2-4,1-1,1-1,1-1,1-1,1-1,1-1"));
        Assertions.assertEquals(1, new Domino().solveDomino("1-1,2-2"));
        Assertions.assertEquals(1, new Domino().solveDomino("1-1"));
        Assertions.assertEquals(1, new Domino().solveDomino("1-2,1-2"));
        Assertions.assertEquals(4, new Domino().solveDomino("3-2,2-1,1-4,4-4,5-4,4-2,2-1"));
        Assertions.assertEquals(7, new Domino().solveDomino("5-5,5-5,4-4,5-5,5-5,5-5,5-5,5-5,5-5,5-5"));
        Assertions.assertEquals(4, new Domino().solveDomino("1-1,3-5,5-5,5-4,4-2,1-3"));
        Assertions.assertEquals(3, new Domino().solveDomino("1-2,2-2,3-3,3-4,4-5,1-1,1-2"));
        Assertions.assertEquals(0, new Domino().solveDomino(""));
    }
    
    @Test
    public void correctFormat() throws Exception {
        Domino solver = new Domino();
        Assertions.assertEquals("First, solve the problem. Then, write the code.", solver.correctFormat("   first    ,   solve    the   problem   .     then  ,    write     the    code  .   "));
        Assertions.assertEquals("First, solve the problem. Then, write the code.", solver.correctFormat("first,solve the problem.then,write the code."));
        Assertions.assertEquals("This is a test... And another test.", solver.correctFormat("this is a test   ...and another test."));
        Assertions.assertEquals("Listen: this is important!", solver.correctFormat("listen    :    this is important!"));
        Assertions.assertEquals("Reasons: reason one; reason two; reason three.", solver.correctFormat("reasons     :reason one;reason two;reason three  .  "));
        Assertions.assertEquals("I. I.", solver.correctFormat(" i  .    i .  "));
    }
}

/*
 "1-1" => 1
"1-2,1-2" => 1
"3-2,2-1,1-4,4-4,5-4,4-2,2-1" => 4
"5-5,5-5,4-4,5-5,5-5,5-5,5-5,5-5,5-5,5-5" => 7
"1-1,3-5,5-5,5-4,4-2,1-3" => 4
"1-2,2-2,3-3,3-4,4-5,1-1,1-2" => 3
*/
