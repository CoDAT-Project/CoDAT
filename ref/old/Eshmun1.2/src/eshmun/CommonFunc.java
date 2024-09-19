package eshmun;

public class CommonFunc {

	public CommonFunc() {
		// TODO Auto-generated constructor stub
	}
	
	public static String FormatInput(String input)
	{
		input = input.replace(" ", "");
		input = InsertBracket("AG(", input);
		input = InsertBracket("AF(", input);
		input = InsertBracket("AX(", input);
		input = InsertBracket("EG(", input);
		input = InsertBracket("EF(", input);
		input = InsertBracket("EX(", input);
		//input = InsertExclamationBracket(input);
		return input;
	}
	private static String InsertExclamationBracket(String input) {
		if(input.contains("!"))
		{
			StringBuilder ctlBuilder = new StringBuilder(input);
			int count = countMatches("!", input);
			int lastIndex = 0;
			int addedChars = 0;
			for(int i =0; i<count; i++)
			{
				addedChars = 0;
				input = ctlBuilder.toString();
				int startIndex = input.indexOf("!", lastIndex ) + 1;
				lastIndex = startIndex;
				if(input.charAt(startIndex) != '(')
				{
					lastIndex = startIndex + 1;
					ctlBuilder.insert((startIndex), '(');
					for(int j=startIndex;j<input.length(); j++)
					{
						if(!Character.isLetter(input.charAt(j)))
						{
							ctlBuilder.insert((j + 1), ')');
							lastIndex = j+1;
						}
					}
				}
			}
			return ctlBuilder.toString();
		}
		return input;
	}
	private static String InsertBracket(String keyword, String input)
	{
		if(input.contains(keyword))
		{
			StringBuilder ctlBuilder = new StringBuilder(input);
			int agCount = countMatches(keyword, input);
			int agLastIndex = 0;
			int addedChars = 0;
			for(int i =0; i<agCount; i++)
			{
				addedChars = 0;
				input = ctlBuilder.toString();
				int startIndex = input.indexOf(keyword, agLastIndex ) + 3;
				agLastIndex = startIndex + 1;
				ctlBuilder.insert((startIndex -2) + (addedChars ++), '['); 
				
				int closecount = 0;
				for(int j=startIndex;j<input.length(); j++)
				{
					if(input.charAt(j) == ')')
					{
						if(closecount == 0)
						{
							ctlBuilder.insert((j + 1) + addedChars, ']');
							break;
						}
						else
						{
							closecount --;
						}
					}
					else if(input.charAt(j) == '(')
					{
						closecount ++;
					}
				}
			}
			return ctlBuilder.toString();
		}
		return input;
	}
	private static int countMatches(String keyword, String text)
	{
		int index = text.indexOf(keyword);
		int count = 0;
		while (index != -1) {
		    count++;
		    text = text.substring(index + keyword.length());
		    index = text.indexOf(keyword);
		}
		return count;
	}

}
