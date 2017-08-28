package org.telosys.tools.cli.commons;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.bundles.TargetDefinition;

public class TargetUtil {

	/**
	 * No constructor ! 
	 */
	private TargetUtil() {
	}
		
	public final static List<TargetDefinition> filter( List<TargetDefinition> allTemplatesDefinitions, List<String> criteria ) {
		List<TargetDefinition> list = select(allTemplatesDefinitions, criteria);
		sort(list);
		return list;
	}
	
	/**
	 * Selects the TargetDefinitions matching the given criteria
	 * @param allTemplatesDefinitions
	 * @param criteria
	 * @return
	 */
	public final static List<TargetDefinition> select( List<TargetDefinition> allTemplatesDefinitions, List<String> criteria ) {
		if ( criteria != null ) {
			Map<String,TargetDefinition> map = new Hashtable<String,TargetDefinition>();
			for ( String criterion : criteria ) {
				for ( TargetDefinition td : allTemplatesDefinitions ) {
					String template = td.getTemplate() ;
					if ( template.contains(criterion) ) {
						//selected.add(td) ;
						// Use map for uniqueness 
						map.put(td.getId(), td); // eg 'ID-String' --> TargetDefinition
					}
				}				
			}
			// Convert map values to list
			List<TargetDefinition> list = new LinkedList<TargetDefinition>();
			for ( TargetDefinition td : map.values() ) {
				list.add(td);
			}
			return list;	
		}
		else {
			return allTemplatesDefinitions ; // No critera => ALL
		}
	}

	/**
	 * Sorts the given list of TargetDefinition by template name
	 * @param list
	 */
	public final static void sort( List<TargetDefinition> list) {
		// Sort 
		Collections.sort(list, new Comparator<TargetDefinition>() {
			@Override
	        public int compare(TargetDefinition td1, TargetDefinition td2) {
	        	return  td1.getTemplate().compareTo(td2.getTemplate());
	        } 
		});
	}
	
	public static List<String> buildList( List<TargetDefinition> targetDefinitions ) {
		if ( targetDefinitions != null && targetDefinitions.size() > 0 ) {
			List<String> list = new LinkedList<String>();
			for ( TargetDefinition td : targetDefinitions ) {
				list.add( buildLine(td) );
			}
			return list;
		}
		else {
			return new LinkedList<String>();
		}
	}

	public static String buildListAsString( List<TargetDefinition> targetDefinitions ) {
		if ( targetDefinitions != null && targetDefinitions.size() > 0 ) {
			StringBuffer sb = new StringBuffer();
			for ( TargetDefinition td : targetDefinitions ) {
				sb.append( buildLine(td) );
				sb.append(Environment.LINE_SEPARATOR);
			}
			return sb.toString();
		}
		else {
			return "No template";
		}
	}

	private static String buildLine(TargetDefinition td) {
		return " . [" + getTargetType(td) + "] " + td.getTemplate() + " -> " + td.getFile() ;
	}

	public static String getTargetType(TargetDefinition td) {
		if ( td.isOnce() ) {
			return "1" ;
		}
		else if ( td.isResource() ) {
			return "R" ;
		}
		else {
			return "*" ;
		}
	}
}
