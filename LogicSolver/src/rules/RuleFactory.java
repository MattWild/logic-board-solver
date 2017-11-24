package rules;

import presentation.MainApp;

public class RuleFactory {
	
	private final MainApp mainApp;

	public RuleFactory(MainApp mainApp) {
		this.mainApp = mainApp;
	};
	
	public Rule createDeclarationRule(String cat1Name, String cat2Name, String opt1Name, String opt2Name, String hitmissString) {
		DeclarationRule rule = new DeclarationRule();
		
		int cat1 = mainApp.getCategoryIndex(cat1Name);
		rule.setCategory1(cat1);
		
		int cat2 = mainApp.getCategoryIndex(cat2Name);
		rule.setCategory2(cat2);
		
		int opt1 = mainApp.getOptionIndex(cat1, opt1Name);
		rule.setOption1(opt1);
		
		int opt2 = mainApp.getOptionIndex(cat2, opt2Name);
		rule.setOption2(opt2);
		
		if (hitmissString.compareTo("is") == 0) {
			rule.setValue(1);
		} else {
			rule.setValue(-1);
		}
		
		return rule;
	}
	
	public Rule createRestrictionRule(String catToRestrictName, String optToRestrictName, String targetCat1Name, String targetCat2Name, String targetOpt1Name, String targetOpt2Name) {
		RestrictionRule rule = new RestrictionRule();
		
		int catToRestrict = mainApp.getCategoryIndex(catToRestrictName);
		rule.setCategoryToRestrict(catToRestrict);
		
		int optToRestrict = mainApp.getOptionIndex(catToRestrict, optToRestrictName);
		rule.setOptionToRestrict(optToRestrict);
		
		int targetCat1 = mainApp.getCategoryIndex(targetCat1Name);
		int targetOpt1 = mainApp.getOptionIndex(targetCat1, targetOpt1Name);
		rule.addTargetOption(targetCat1, targetOpt1);
		
		int targetCat2 = mainApp.getCategoryIndex(targetCat2Name);
		int targetOpt2 = mainApp.getOptionIndex(targetCat2, targetOpt2Name);
		rule.addTargetOption(targetCat2, targetOpt2);
		
		return rule;
	}
	
	public Rule createRelationRule(String mainCategoryName, String greaterCategoryName, String greaterOptionName, String lesserCategoryName, String lesserOptionName, String valueString) {
		RelationRule rule = new RelationRule();
		
		int lesserCategory = mainApp.getCategoryIndex(lesserCategoryName);
		rule.setCategory1(lesserCategory);
		
		int greaterCategory = mainApp.getCategoryIndex(greaterCategoryName);
		rule.setCategory2(greaterCategory);
		
		int lesserOption = mainApp.getOptionIndex(lesserCategory, lesserOptionName);
		rule.setOption1(lesserOption);
		
		int greaterOption = mainApp.getOptionIndex(greaterCategory, greaterOptionName);
		rule.setOption2(greaterOption);
		
		System.out.println(mainCategoryName);
		int mainCategory = mainApp.getCategoryIndex(mainCategoryName);
		System.out.println(mainCategory);
		rule.setMainCategory(mainCategory);
		
		if (valueString.compareTo("N/A") == 0) {
			rule.setDifference(0);
		} else {
			rule.setDifference(Integer.parseInt(valueString));
		}
		
		return rule;
	}
	
	public Rule createDoubleRestrictionRule(String category00Name, String category01Name, String category10Name, String category11Name, String option00Name, String option01Name, String option10Name, String option11Name) {
		DoubleRestrictionRule rule = new DoubleRestrictionRule();
		
		int category00 = mainApp.getCategoryIndex(category00Name);
		rule.setFirstCategoryInFirstPair(category00);
		
		int category01 = mainApp.getCategoryIndex(category01Name);
		rule.setSecondCategoryInFirstPair(category01);
		
		int category10 = mainApp.getCategoryIndex(category10Name);
		rule.setFirstCategoryInSecondPair(category10);
		
		int category11 = mainApp.getCategoryIndex(category11Name);
		rule.setSecondCategoryInSecondPair(category11);
		
		int option00 = mainApp.getOptionIndex(category00, option00Name);
		rule.setFirstOptionInFirstPair(option00);
		
		int option01 = mainApp.getOptionIndex(category01, option01Name);
		rule.setSecondOptionInFirstPair(option01);
		
		int option10 = mainApp.getOptionIndex(category10, option10Name);
		rule.setFirstOptionInSecondPair(option10);
		
		int option11 = mainApp.getOptionIndex(category11, option11Name);
		rule.setSecondOptionInSecondPair(option11);
		
		return rule;
	}
}
