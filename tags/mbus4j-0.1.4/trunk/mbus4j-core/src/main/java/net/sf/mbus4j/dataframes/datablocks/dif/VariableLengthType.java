/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.dif;

/**
 *
 * @author aploese
 */
public enum VariableLengthType {

    STRING("String"),
    BIG_DECIMAL("Big Decimal");

    private final String label;
    
    private VariableLengthType(String label )
    {
        this.label = label;
    }

    @Override
    public String toString(  )
    {
        return label;
    }

    /**
     * @return the label
     */
    public String getLabel(  )
    {
        return label;
    }

    public static VariableLengthType fromLabel( String label )
    {
        for ( VariableLengthType value : values(  ) )
        {
            if ( value.getLabel(  ).equals( label ) )
            {
                return value;
            }
        }

        return valueOf( label );
    }

}
