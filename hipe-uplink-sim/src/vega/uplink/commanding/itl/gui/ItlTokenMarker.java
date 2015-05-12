package vega.uplink.commanding.itl.gui;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.text.Segment;

import vega.uplink.Properties;
import vega.uplink.commanding.Mib;
import vega.uplink.track.Fecs;

public class ItlTokenMarker extends TokenMarker {
	// ~ Static variables/initializers
	// ----------------------------------------------------
	// private members
	private static KeywordMap cKeywords;
	// ~ Instance variables
	// ---------------------------------------------------------------
	private KeywordMap keywords;
	private boolean cpp;
	private static final Logger LOG = Logger.getLogger(ItlTokenMarker.class.getName());

	// ~ Constructors
	// ---------------------------------------------------------------------
	/**
	 * Creates a new JavaTokenMarker object.
	 */
	public ItlTokenMarker() {
		this(true, getKeywords());
	}

	/**
	 * Creates a new JavaTokenMarker object.
	 * 
	 * @param cpp
	 *            DOCUMENT ME!
	 * @param keywords
	 *            DOCUMENT ME!
	 */
	public ItlTokenMarker(boolean cpp, KeywordMap keywords) {
		this.cpp = cpp;
		this.keywords = keywords;
	}

	// ~ Methods
	// --------------------------------------------------------------------------
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static KeywordMap getKeywords() {


		List<String> instruments = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		if (cKeywords == null) {
			cKeywords = new KeywordMap(false);
		}
		/*try{
			Mib mib=Mib.getMib();
			String[] allSequences=mib.getAllSequences();
			LOG.info("Adding "+allSequences.length+" sequences from the mib as keywords.");
			for (int i=0;i<allSequences.length;i++){
				cKeywords.add(allSequences[i], Token.KEYWORD2);
				//System.out.println("Added "+allSequences[i]);
			}
		}catch (Exception e){
			LOG.info("Could not get mib:"+e.getMessage());
		}*/
		Iterator<String> it = instruments.iterator();
		while (it.hasNext()){
			cKeywords.add(it.next(), Token.LITERAL1);
		}
		cKeywords.add("Init_mode:", Token.KEYWORD1);
		cKeywords.add("Init_MS:", Token.KEYWORD1);
		cKeywords.add("Init_memory:", Token.KEYWORD1);
		cKeywords.add("Init_data_store:", Token.KEYWORD1);
		cKeywords.add("Version:", Token.KEYWORD1);
		cKeywords.add("Ref_date:", Token.KEYWORD1);
		cKeywords.add("Start_time:", Token.KEYWORD1);
		cKeywords.add("End_time:", Token.KEYWORD1);		
		cKeywords.add("DATA_RATE_PROFILE", Token.KEYWORD2);
		cKeywords.add("POWER_PROFILE", Token.KEYWORD2);
		cKeywords.add("REPEAT", Token.KEYWORD2);
		cKeywords.add("SEPARATION", Token.KEYWORD2);
		cKeywords.add("bits/sec", Token.KEYWORD3);
		//cKeywords.add("sec", Token.KEYWORD3);	
		cKeywords.add("Watts", Token.KEYWORD3);	
		cKeywords.add("Raw", Token.KEYWORD3);		

		cKeywords.add("START_OBS", Token.LITERAL1);
		cKeywords.add("END_OBS", Token.LITERAL1);

		cKeywords.add("this", Token.LITERAL2);
		cKeywords.add("null", Token.LITERAL2);
		cKeywords.add("super", Token.LITERAL2);
		cKeywords.add("true", Token.LITERAL2);
		cKeywords.add("false", Token.LITERAL2);

		return cKeywords;
	}

	/**
	 * Marks the token
	 * 
	 * @param token
	 *            The token to mark
	 * @param line
	 *            The line it is on
	 * @param lineIndex
	 *            The index of the line
	 * 
	 * @return DOCUMENT ME!
	 */
	public byte markTokensImpl(byte token, Segment line, int lineIndex) {
		char[] array = line.array;
		int offset = line.offset;
		int lastOffset = offset;
		int lastKeyword = offset;
		int newLength = line.count + offset;
		boolean backslash = false;
		loop: for (int i = offset; i < newLength; i++) {
			int i1 = (i + 1);
			char c = array[i];
			switch (c) {
			case '\\':
				backslash = !backslash;
				break;
			case '*':
				if (((token == Token.COMMENT1) || (token == Token.COMMENT2) || (token == Token.COMMENT3))
						&& ((newLength - i) > 1)) {
					backslash = false;
					if (((newLength - i) > 1) && (array[i1] == '/')) {
						i++;
						addToken((i + 1) - lastOffset, token);
						token = Token.NULL;
						lastOffset = i + 1;
						lastKeyword = lastOffset;
					}
				}
				break;
			case '#':
				/*backslash = false;
				if (cpp && (token == Token.NULL)) {
					token = Token.COMMENT1;
					addToken(i - lastOffset, Token.NULL);
					addToken(newLength - i, Token.COMMENT1);
					lastOffset = newLength;
					break loop;
				}
				break;*/
				addToken(i - lastOffset, token);
				addToken(newLength - i, Token.COMMENT1);
				lastOffset = newLength;
				lastKeyword = lastOffset;
				break loop;
			case '/':
				backslash = false;
				if ((token == Token.NULL) && ((newLength - i) > 1)) {
					switch (array[i1]) {
					case '*':
						addToken(i - lastOffset, token);
						lastOffset = i;
						lastKeyword = lastOffset;
						if (((newLength - i) > 2) && (array[i + 2] == '*')) {
							token = Token.COMMENT2;
						} else if (((newLength - i) > 2)
								&& (array[i + 2] == '#')) {
							token = Token.COMMENT3;
						} else {
							token = Token.COMMENT1;
						}
						break;
					case '/':
						addToken(i - lastOffset, token);
						addToken(newLength - i, Token.COMMENT1);
						lastOffset = newLength;
						lastKeyword = lastOffset;
						break loop;
					}
				}
				break;
			case '"':
				if (backslash) {
					backslash = false;
				} else if (token == Token.NULL) {
					token = Token.LITERAL1;
					addToken(i - lastOffset, Token.NULL);
					lastOffset = i;
					lastKeyword = lastOffset;
				} else if (token == Token.LITERAL1) {
					token = Token.NULL;
					addToken(i1 - lastOffset, Token.LITERAL1);
					lastOffset = i1;
					lastKeyword = lastOffset;
				}
				break;
			case '\'':
				if (backslash) {
					backslash = false;
				} else if (token == Token.NULL) {
					token = Token.LITERAL2;
					addToken(i - lastOffset, Token.NULL);
					lastOffset = i;
					lastKeyword = lastOffset;
				} else if (token == Token.LITERAL2) {
					token = Token.NULL;
					addToken(i1 - lastOffset, Token.LITERAL1);
					lastOffset = i1;
					lastKeyword = lastOffset;
				}
				break;
			case ':':
				if ((token == Token.NULL) && (lastKeyword == offset)) {
					backslash = false;
					addToken(i1 - lastOffset, Token.LABEL);
					lastOffset = i1;
					lastKeyword = lastOffset;
					break;
				}
			default:
				backslash = false;
				if ((token == Token.NULL) && (c != '_')
						&& !(Character.isLetter(c) || c == '@')) {
					int len = i - lastKeyword;
					byte id = keywords.lookup(line, lastKeyword, len);
					if (id != Token.NULL) {
						if (lastKeyword != lastOffset) {
							addToken(lastKeyword - lastOffset, Token.NULL);
						}
						addToken(len, id);
						lastOffset = i;
					}
					lastKeyword = i1;
				}
				break;
			}
		}
		if (token == Token.NULL) {
			int len = newLength - lastKeyword;
			byte id = keywords.lookup(line, lastKeyword, len);
			if (id != Token.NULL) {
				if (lastKeyword != lastOffset) {
					addToken(lastKeyword - lastOffset, Token.NULL);
				}
				addToken(len, id);
				lastOffset = newLength;
			}
		}
		if (lastOffset != newLength) {
			if ((token == Token.LITERAL1) || (token == Token.LITERAL2)) {
				addToken(newLength - lastOffset, Token.INVALID);
				token = Token.NULL;
			} else {
				addToken(newLength - lastOffset, token);
			}
		}
		if ((token == Token.KEYWORD2) && !backslash) {
			token = Token.NULL;
		}
		return token;
	}

}
