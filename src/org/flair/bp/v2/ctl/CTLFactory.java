package org.flair.bp.v2.ctl;

import java.util.List;

import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.path.Finally;
import org.flair.bp.v2.ctl.parts.path.Globally;
import org.flair.bp.v2.ctl.parts.path.Next;
import org.flair.bp.v2.ctl.parts.path.PathQuantifier;
import org.flair.bp.v2.ctl.parts.path.Until;
import org.flair.bp.v2.ctl.parts.state.All;
import org.flair.bp.v2.ctl.parts.state.And;
import org.flair.bp.v2.ctl.parts.state.Exists;
import org.flair.bp.v2.ctl.parts.state.Negation;
import org.flair.bp.v2.ctl.parts.state.Or;
import org.flair.bp.v2.ctl.parts.state.Proposition;
import org.flair.bp.v2.ctl.parts.state.StateQuantifier;
import org.flair.bp.v2.ctl.util.BadFormattedCTLException;


/********************
 * 
 * DEPRICATED
 *
 */

public class CTLFactory
{
	public static final String AX = "AX"; //All Next
	public static final String EX = "EX"; //Exists Next
	public static final String AF = "AF"; //All Finally
	public static final String EF = "EF"; //Exists Finally
	public static final String AG = "AG"; //All Globally
	public static final String EG = "EG"; //Exists Globally
	public static final String AU = "AU"; //All Until
	public static final String EU = "EU"; //Exists Until
	
	public CTLFactory(){}
	

	public static CTLImplies getSimpleCTLFormula(Proposition s, String CTL, Proposition t) throws BadFormattedCTLException
	{
		// s => CTL t(0)
		// example: s => EF t
		
		Phi phi;
		PathQuantifier pq;
		
		char[] c = CTL.toCharArray();
		
		if(c[1] == 'X')
			pq = (PathQuantifier) new Next((Phi)t);
		else if(c[1] == 'F')
			pq = (PathQuantifier) new Finally((Phi)t);
		else if(c[1] == 'G')
			pq = (PathQuantifier) new Globally((Phi)t);
		else throw new BadFormattedCTLException("Missing Token in simple CTL: X, F, or G expected.");
		
		if(c[0] == 'E')
			phi = (Phi)new Exists(pq);
		else if(c[0] == 'A')
			phi = (Phi)new All(pq);
		else throw new BadFormattedCTLException("Missing Token in simple CTL: A, or E expected.");
		
		return new CTLImplies(s, (StateQuantifier) phi);
	}
	
	public static CTLImplies getSimpleCTLFormula(Proposition s, String CTL, List<Proposition> t) throws BadFormattedCTLException
	{
		// s => CTL t(0)
		// example: s => EF t(0)
		// example2: s => E t(0) U t(1)
		
		if(t.size() == 0) return null;
		
		Phi phi;
		PathQuantifier pq;
		
		char[] c = CTL.toCharArray();
		
		if(c[1] == 'X')
			pq = (PathQuantifier) new Next((Phi)t.get(0));
		else if(c[1] == 'U' && t.size() > 2)
			pq = (PathQuantifier) new Until((Phi)t.get(0),(Phi)t.get(1));
		else if(c[1] == 'F')
			pq = (PathQuantifier) new Finally((Phi)t.get(0));
		else if(c[1] == 'G')
			pq = (PathQuantifier) new Globally((Phi)t.get(0));
		else throw new BadFormattedCTLException("Missing Token in simple CTL: X, F, G, or U expected.");
		
		if(c[0] == 'E')
			phi = (Phi)new Exists(pq);
		else if(c[0] == 'E')
			phi = (Phi)new All(pq);
		else  throw new BadFormattedCTLException("Missing Token in simple CTL: A, or E expected.");
		
		return new CTLImplies(s, (StateQuantifier) phi);
	}
	
	public static CTLImplies getComplexCTLFormula(List<Proposition> p, String CTL) throws BadFormattedCTLException
	{
		//CTL example: 0 => EFA((1 OR 2 OR 3)U 4)
		//Numbers point to list index;
		String fctl = getFormattedCTL(CTL);
		
		int i = 0;
		while(fctl.charAt(i) != '=' && i<fctl.length()) i++;
		
		if(i==fctl.length()) throw new BadFormattedCTLException("Missing implies token.");
		String pre = fctl.substring(0, i);
		String post = fctl.substring(i+1, fctl.length());
		
		int preIndex = -1;
		Proposition prop = null;
		
		try
		{
			preIndex = Integer.parseInt(pre.trim());
			prop = p.get(preIndex);
		}
		catch(Exception e)
		{
			throw new BadFormattedCTLException("Malformed token before implies.");
		}
		
		Phi phi = parseCTL(p, post);
		
		return new CTLImplies(prop, (StateQuantifier) phi);
	}
	
	private static String getFormattedCTL(String CTL)
	{
		CTL = CTL.toUpperCase();
		CTL = CTL.replaceAll("OR", "|");
		CTL = CTL.replaceAll("AND", "&");
		CTL = CTL.replaceAll("->", "=");
		CTL = CTL.replaceAll("=>", "=");
		CTL = CTL.replaceAll("<->", "=");
		CTL = CTL.replaceAll("<=>", "=");
		CTL = CTL.replaceAll("NOT ", "-");
		return CTL.trim();
	}
	
	private static Phi parseCTL(List<Proposition> p, String fctl) throws BadFormattedCTLException
	{
		int i =0;
		Phi phi = null;
		StateQuantifier q = null;
		PathQuantifier pq = null;
		
		//System.out.println("Parsing.. found: " + fctl);
		
		//find next token
		while(i<fctl.length() && fctl.charAt(i) == ' ') i++;
		
		//reached end?
		if(i==fctl.length()) throw new BadFormattedCTLException("Unexpected end of String. " + fctl);
		
		//if token is A or E, create it and check next
		if(fctl.charAt(i) == 'A' || fctl.charAt(i) == 'E')
		{
			if(fctl.charAt(i) == 'A')
				q = (StateQuantifier)new All();
			else
				q = (StateQuantifier)new Exists();
			
			if(i++<fctl.length())
			{
				if(fctl.charAt(i) == 'X')
					pq = (PathQuantifier)new Next(parseCTL(p, fctl.substring(i+1).trim()));
				else if(fctl.charAt(i) == 'F')
					pq = (PathQuantifier)new Finally(parseCTL(p, fctl.substring(i+1).trim()));
				else if(fctl.charAt(i) == 'G')
					pq = (PathQuantifier)new Globally(parseCTL(p, fctl.substring(i+1).trim()));
				else if(fctl.charAt(i) == '[') //U
				{
					//find U
					int j = i+1;
					while(j<fctl.length() && fctl.charAt(j) != 'U')
					{
						//skip nested until's
						if(fctl.charAt(j) == '[') j = findMatchingSquareClosingBracket(fctl, j+1);
						j++;
					}
					if(j==fctl.length()) throw new BadFormattedCTLException("Unexpected end of String.");
					
					//find ]
					int k = j+1;
					while(k<fctl.length() && fctl.charAt(k) != ']')
					{
						//skip nested until's
						if(fctl.charAt(k) == '[') k = findMatchingSquareClosingBracket(fctl, k+1);
						k++;
					}
					if(k==fctl.length()) throw new BadFormattedCTLException("Unexpected end of String.");
					
					pq = (PathQuantifier)new Until(parseCTL(p, fctl.substring(i+1,j).trim()), parseCTL(p, fctl.substring(j+1,k).trim()));
				}
				else throw new BadFormattedCTLException("Bad token encountered at String index: " + i + ".");
				
				q.setPathQuantifier(pq);
				phi = (Phi)q;
			}
			else throw new BadFormattedCTLException("Unexpected end of String.");
		}
		else if(fctl.charAt(i) == '(')  //if token is OR or AND
		{
			//find matching ')'
			int j = findMatchingRoundClosingBracket(fctl,i+1);
			
			if(j==fctl.length()) throw new BadFormattedCTLException("Unexpected end of String.");
			
			//take part between brackets
			String subs = fctl.substring(i+1, j).trim();
			//filter out sub bracketed parts
			String subs2 = subs;
			int k = 0;
			while(k<subs2.length())
			{
				if(subs2.charAt(k) == '(')
				{
					int l = findMatchingRoundClosingBracket(subs2, k+1);
					subs2 = subs2.substring(0, k) + subs2.substring(l);
					k--;
				}
				k++;
			}
			
			int firstAndIndex = subs2.indexOf("&");
			int firstOrIndex = subs2.indexOf("|");
			
			//Is it Or or And?
			if(firstAndIndex == firstOrIndex) throw new BadFormattedCTLException("And or Or token expected at String index: " + i+1 + ".");
			else if(firstAndIndex == -1) phi = (Phi)new Or();
			else if(firstOrIndex == -1) phi = (Phi)new And();
			else throw new BadFormattedCTLException("Mixed And & Or tokens found from String index: " + i+1 + ".");
			
			//split around found token, but skip sub-bracketed parts
			char c = (phi instanceof Or) ? '|' : '&';
			k = 0;
			int lastToken = -1;
			subs = subs + c;
			while(k<subs.length())
			{
				//skip bracketed parts
				if(subs.charAt(k) == '(') k = findMatchingRoundClosingBracket(subs, k+1)-1;
				else if(subs.charAt(k) == c)
				{
					if(phi instanceof Or) ((Or)phi).addPhi(parseCTL(p, subs.substring(lastToken + 1, k).trim()));
					else ((And)phi).addPhi(parseCTL(p, subs.substring(lastToken + 1, k).trim()));
					lastToken = k;
				}
				k++;
			}
		}
		else if(fctl.charAt(i) == '-')  //if negation
		{
			phi = (Phi)new Negation((StateQuantifier)parseCTL(p, fctl.substring(i + 1).trim()));
		}
		else //must be proposition
		{
			//find end of number
			int k = i;
			while(k<fctl.length() && fctl.charAt(k) != ' ' && fctl.charAt(k) != ')' && fctl.charAt(k) != ']') k++;
			
			try
			{
				phi = (Phi)p.get(Integer.parseInt(fctl.substring(i, k).trim()));
			}
			catch(Exception e)
			{
				throw new BadFormattedCTLException("Could not add proposition from token at String index: " + i + ". "+fctl);
			}
		}
		
		return phi;
	}

	private static int findMatchingSquareClosingBracket(String s, int startFrom)
	{
		return findMatchingClosingBracket(s, '[', ']', startFrom);
	}

	private static int findMatchingRoundClosingBracket(String s, int startFrom)
	{
		return findMatchingClosingBracket(s, '(', ')', startFrom);
	}
	
	private static int findMatchingClosingBracket(String s, char open, char close, int startFrom)
	{
		int j = startFrom;
		int openCount = 1;

		while(openCount > 0 && j<s.length())
		{
			if(s.charAt(j) == open) openCount++;
			else if(s.charAt(j) == close) openCount--;
			
			if(openCount > 0) j++;
		}
		
		return j;
	}
}
