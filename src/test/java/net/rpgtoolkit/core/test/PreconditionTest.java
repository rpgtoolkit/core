/*
 */
package net.rpgtoolkit.core.test;

import net.rpgtoolkit.core.Precondition;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for preconditions.
 * 
 * @see Precondition
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class PreconditionTest {
    
    @Test
    public void notNull() {
        
        Object a = new Object();
        Object b = null;
        
        // Ensure a is not null
        
        Precondition.notNull(a);
        
        // Ensure notNull throws when given a null reference
        
        try {
            Precondition.notNull(b);
            Assert.fail();
        }
        catch (IllegalArgumentException ex) {
            // pass
        }
        
    }
    
    @Test
    public void rangeTestMin() {
        
        String name = "arg";
        int min = 0;
        int max = 100;
        
        // Ensure range min argument is in range
                
        Precondition.range(name, min, max, min);
        
        // Ensure arbitrary argument is in range
        
        Precondition.range(name, min, max, (max - min) / 2);
        
        // Ensure below minimum is illegal
        
        try {
            Precondition.range(name, min, max, min - 1);
            Assert.fail();
        }
        catch (IllegalArgumentException ex) {
            // pass
        }
        
    }
    
    @Test
    public void rangeTestMax() {
        
        String name = "arg";
        int min = 0;
        int max = 100;
        
        // Ensure range max argument is in range
        
        Precondition.range(name, min, max, max);
        
        // Ensure arbitrary value is in range
        
        Precondition.range(name, min, max, (max - min) / 2);
        
        // Ensure above maximum is illegal
        
        try {
            Precondition.range(name, min, max, max + 1);
            Assert.fail();
        }
        catch (IllegalArgumentException ex) {
            // pass
        }
        
    }
    
}
