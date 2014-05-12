package org.albianj.algorithm;

//import net.sourceforge.sizeof.SizeOf;

import org.albianj.verify.Validate;

public class Hash
{
	//
	// #define BITS_IN_int (sizeof (int) * CHAR_BIT)
	// #define THREE_QUARTERS ((int) ((BITS_IN_int * 3) / 4))
	// #define ONE_EIGHTH ((int) (BITS_IN_int / 8 ))
	// #define HIGH_BITS ( ~((unsighed int)(~0) >> ONE_EIGHTH ))
	//
	// unsigned int HashPJW ( const char * datum )
	// {
	// unsigned int hash_value, i;
	//
	// for ( hash_value = 0; *datum; ++datum)
	// {
	// hash_value = ( hash_value << ONE_EIGHTH ) + *datum;
	// if (( i = hash_value & HIGH_BITS ) !=0 )
	// hash_value =
	// ( hash_value ^ ( i>> THREE_QUARTERS )) &
	// ~HIGH_BITS;
	// }
	// return ( hash_value );
	// }

	private static final int BITS_IN_int = 32;
	private static final int THREE_QUARTERS = ((int) ((BITS_IN_int * 3) / 4));
	private static final int ONE_EIGHTH = ((int) (BITS_IN_int / 8));
	private static final int HIGH_BITS = (~(((~0) & 0xff) >> ONE_EIGHTH)) & 0xff;

	public static int hashPJW(String value)
	{
		if (Validate.isNullOrEmpty(value)) { throw new IllegalArgumentException(
				"value"); }
		int hash_value, i, j;
		char[] datum = value.toCharArray();
		for (hash_value = 0, j = 0; j < datum.length; j++)
		{
			hash_value = (hash_value << ONE_EIGHTH) + datum[j];
			if ((i = hash_value & HIGH_BITS) != 0) hash_value = (hash_value ^ (i >> THREE_QUARTERS))
					& ~HIGH_BITS;
		}
		return (Math.abs(hash_value));
	}

	public static long generateHashCode(String value)
			throws IllegalArgumentException
	{
		if (Validate.isNullOrEmpty(value)) { throw new IllegalArgumentException(
				"value"); }
		int len = value.length();
		long llen = (long) len;
		int step = (len >> 5) + 1;
		for (int i = len; i >= step; i -= step)
		{
			llen = llen ^ ((llen << 5) + (llen >> 2) + value.charAt(i - 1));
		}
		return Math.abs(llen);
	}
}
