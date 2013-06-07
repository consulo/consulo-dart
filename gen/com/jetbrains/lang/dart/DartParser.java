// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart;

import org.jetbrains.annotations.*;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.openapi.diagnostic.Logger;
import static com.jetbrains.lang.dart.DartTokenTypes.*;
import static com.jetbrains.lang.dart.DartGeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class DartParser implements PsiParser {

  public static Logger LOG_ = Logger.getInstance("com.jetbrains.lang.dart.DartParser");

  @NotNull
  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    int level_ = 0;
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this);
    if (root_ == ABSTRACT_OPERATOR_DECLARATION) {
      result_ = abstractOperatorDeclaration(builder_, level_ + 1);
    }
    else if (root_ == ADDITIVE_EXPRESSION) {
      result_ = additiveExpression(builder_, level_ + 1);
    }
    else if (root_ == ADDITIVE_OPERATOR) {
      result_ = additiveOperator(builder_, level_ + 1);
    }
    else if (root_ == ARGUMENT_LIST) {
      result_ = argumentList(builder_, level_ + 1);
    }
    else if (root_ == ARGUMENTS) {
      result_ = arguments(builder_, level_ + 1);
    }
    else if (root_ == ARRAY_ACCESS_EXPRESSION) {
      result_ = arrayAccessExpression(builder_, level_ + 1);
    }
    else if (root_ == AS_EXPRESSION) {
      result_ = asExpression(builder_, level_ + 1);
    }
    else if (root_ == ASSERT_STATEMENT) {
      result_ = assertStatement(builder_, level_ + 1);
    }
    else if (root_ == ASSIGN_EXPRESSION) {
      result_ = assignExpression(builder_, level_ + 1);
    }
    else if (root_ == ASSIGNMENT_OPERATOR) {
      result_ = assignmentOperator(builder_, level_ + 1);
    }
    else if (root_ == BITWISE_EXPRESSION) {
      result_ = bitwiseExpression(builder_, level_ + 1);
    }
    else if (root_ == BITWISE_OPERATOR) {
      result_ = bitwiseOperator(builder_, level_ + 1);
    }
    else if (root_ == BLOCK) {
      result_ = block(builder_, level_ + 1);
    }
    else if (root_ == BREAK_STATEMENT) {
      result_ = breakStatement(builder_, level_ + 1);
    }
    else if (root_ == CALL_EXPRESSION) {
      result_ = callExpression(builder_, level_ + 1);
    }
    else if (root_ == CASCADE_REFERENCE_EXPRESSION) {
      result_ = cascadeReferenceExpression(builder_, level_ + 1);
    }
    else if (root_ == CATCH_PART) {
      result_ = catchPart(builder_, level_ + 1);
    }
    else if (root_ == CLASS_BODY) {
      result_ = classBody(builder_, level_ + 1);
    }
    else if (root_ == CLASS_DEFINITION) {
      result_ = classDefinition(builder_, level_ + 1);
    }
    else if (root_ == COMPARE_EXPRESSION) {
      result_ = compareExpression(builder_, level_ + 1);
    }
    else if (root_ == COMPONENT_NAME) {
      result_ = componentName(builder_, level_ + 1);
    }
    else if (root_ == COMPOUND_LITERAL_EXPRESSION) {
      result_ = compoundLiteralExpression(builder_, level_ + 1);
    }
    else if (root_ == CONST_CONSTRUCTOR_EXPRESSION) {
      result_ = constConstructorExpression(builder_, level_ + 1);
    }
    else if (root_ == CONTINUE_STATEMENT) {
      result_ = continueStatement(builder_, level_ + 1);
    }
    else if (root_ == DEFAULT_CASE) {
      result_ = defaultCase(builder_, level_ + 1);
    }
    else if (root_ == DEFAULT_FACTROY) {
      result_ = defaultFactroy(builder_, level_ + 1);
    }
    else if (root_ == DEFAULT_FORMAL_NAMED_PARAMETER) {
      result_ = defaultFormalNamedParameter(builder_, level_ + 1);
    }
    else if (root_ == DO_WHILE_STATEMENT) {
      result_ = doWhileStatement(builder_, level_ + 1);
    }
    else if (root_ == EQUALITY_OPERATOR) {
      result_ = equalityOperator(builder_, level_ + 1);
    }
    else if (root_ == EXPORT_STATEMENT) {
      result_ = exportStatement(builder_, level_ + 1);
    }
    else if (root_ == EXPRESSION) {
      result_ = expression(builder_, level_ + 1);
    }
    else if (root_ == EXPRESSION_LIST) {
      result_ = expressionList(builder_, level_ + 1);
    }
    else if (root_ == FACTORY_CONSTRUCTOR_DECLARATION) {
      result_ = factoryConstructorDeclaration(builder_, level_ + 1);
    }
    else if (root_ == FACTORY_SPECIFICATION) {
      result_ = factorySpecification(builder_, level_ + 1);
    }
    else if (root_ == FIELD_FORMAL_PARAMETER) {
      result_ = fieldFormalParameter(builder_, level_ + 1);
    }
    else if (root_ == FIELD_INITIALIZER) {
      result_ = fieldInitializer(builder_, level_ + 1);
    }
    else if (root_ == FINAL_VAR_OR_TYPE) {
      result_ = finalVarOrType(builder_, level_ + 1);
    }
    else if (root_ == FINALLY_PART) {
      result_ = finallyPart(builder_, level_ + 1);
    }
    else if (root_ == FOR_IN_PART) {
      result_ = forInPart(builder_, level_ + 1);
    }
    else if (root_ == FOR_LOOP_PARTS) {
      result_ = forLoopParts(builder_, level_ + 1);
    }
    else if (root_ == FOR_LOOP_PARTS_IN_BRACES) {
      result_ = forLoopPartsInBraces(builder_, level_ + 1);
    }
    else if (root_ == FOR_STATEMENT) {
      result_ = forStatement(builder_, level_ + 1);
    }
    else if (root_ == FORMAL_PARAMETER_LIST) {
      result_ = formalParameterList(builder_, level_ + 1);
    }
    else if (root_ == FUNCTION_BODY) {
      result_ = functionBody(builder_, level_ + 1);
    }
    else if (root_ == FUNCTION_DECLARATION) {
      result_ = functionDeclaration(builder_, level_ + 1);
    }
    else if (root_ == FUNCTION_DECLARATION_WITH_BODY) {
      result_ = functionDeclarationWithBody(builder_, level_ + 1);
    }
    else if (root_ == FUNCTION_DECLARATION_WITH_BODY_OR_NATIVE) {
      result_ = functionDeclarationWithBodyOrNative(builder_, level_ + 1);
    }
    else if (root_ == FUNCTION_EXPRESSION) {
      result_ = functionExpression(builder_, level_ + 1);
    }
    else if (root_ == FUNCTION_EXPRESSION_BODY) {
      result_ = functionExpressionBody(builder_, level_ + 1);
    }
    else if (root_ == FUNCTION_TYPE_ALIAS) {
      result_ = functionTypeAlias(builder_, level_ + 1);
    }
    else if (root_ == GETTER_DECLARATION) {
      result_ = getterDeclaration(builder_, level_ + 1);
    }
    else if (root_ == HIDE_COMBINATOR) {
      result_ = hideCombinator(builder_, level_ + 1);
    }
    else if (root_ == ID) {
      result_ = id(builder_, level_ + 1);
    }
    else if (root_ == IF_STATEMENT) {
      result_ = ifStatement(builder_, level_ + 1);
    }
    else if (root_ == IMPORT_STATEMENT) {
      result_ = importStatement(builder_, level_ + 1);
    }
    else if (root_ == INITIALIZERS) {
      result_ = initializers(builder_, level_ + 1);
    }
    else if (root_ == INTERFACE_BODY) {
      result_ = interfaceBody(builder_, level_ + 1);
    }
    else if (root_ == INTERFACE_DEFINITION) {
      result_ = interfaceDefinition(builder_, level_ + 1);
    }
    else if (root_ == INTERFACES) {
      result_ = interfaces(builder_, level_ + 1);
    }
    else if (root_ == IS_EXPRESSION) {
      result_ = isExpression(builder_, level_ + 1);
    }
    else if (root_ == ITERATOR_EXPRESSION) {
      result_ = iteratorExpression(builder_, level_ + 1);
    }
    else if (root_ == LABEL) {
      result_ = label(builder_, level_ + 1);
    }
    else if (root_ == LIBRARY_COMPONENT_REFERENCE_EXPRESSION) {
      result_ = libraryComponentReferenceExpression(builder_, level_ + 1);
    }
    else if (root_ == LIBRARY_ID) {
      result_ = libraryId(builder_, level_ + 1);
    }
    else if (root_ == LIBRARY_REFERENCE_LIST) {
      result_ = libraryReferenceList(builder_, level_ + 1);
    }
    else if (root_ == LIBRARY_STATEMENT) {
      result_ = libraryStatement(builder_, level_ + 1);
    }
    else if (root_ == LIST_LITERAL_EXPRESSION) {
      result_ = listLiteralExpression(builder_, level_ + 1);
    }
    else if (root_ == LITERAL_EXPRESSION) {
      result_ = literalExpression(builder_, level_ + 1);
    }
    else if (root_ == LOGIC_AND_EXPRESSION) {
      result_ = logicAndExpression(builder_, level_ + 1);
    }
    else if (root_ == LOGIC_OR_EXPRESSION) {
      result_ = logicOrExpression(builder_, level_ + 1);
    }
    else if (root_ == LONG_TEMPLATE_ENTRY) {
      result_ = longTemplateEntry(builder_, level_ + 1);
    }
    else if (root_ == MAP_LITERAL_ENTRY) {
      result_ = mapLiteralEntry(builder_, level_ + 1);
    }
    else if (root_ == MAP_LITERAL_EXPRESSION) {
      result_ = mapLiteralExpression(builder_, level_ + 1);
    }
    else if (root_ == METADATA) {
      result_ = metadata(builder_, level_ + 1);
    }
    else if (root_ == METHOD_DECLARATION) {
      result_ = methodDeclaration(builder_, level_ + 1);
    }
    else if (root_ == METHOD_PROTOTYPE_DECLARATION) {
      result_ = methodPrototypeDeclaration(builder_, level_ + 1);
    }
    else if (root_ == MIXINS) {
      result_ = mixins(builder_, level_ + 1);
    }
    else if (root_ == MULTIPLICATIVE_EXPRESSION) {
      result_ = multiplicativeExpression(builder_, level_ + 1);
    }
    else if (root_ == MULTIPLICATIVE_OPERATOR) {
      result_ = multiplicativeOperator(builder_, level_ + 1);
    }
    else if (root_ == NAMED_ARGUMENT) {
      result_ = namedArgument(builder_, level_ + 1);
    }
    else if (root_ == NAMED_CONSTRUCTOR_DECLARATION) {
      result_ = namedConstructorDeclaration(builder_, level_ + 1);
    }
    else if (root_ == NAMED_FORMAL_PARAMETERS) {
      result_ = namedFormalParameters(builder_, level_ + 1);
    }
    else if (root_ == NATIVE_STATEMENT) {
      result_ = nativeStatement(builder_, level_ + 1);
    }
    else if (root_ == NEW_EXPRESSION) {
      result_ = newExpression(builder_, level_ + 1);
    }
    else if (root_ == NORMAL_FORMAL_PARAMETER) {
      result_ = normalFormalParameter(builder_, level_ + 1);
    }
    else if (root_ == ON_PART) {
      result_ = onPart(builder_, level_ + 1);
    }
    else if (root_ == OPERATOR_DECLARATION) {
      result_ = operatorDeclaration(builder_, level_ + 1);
    }
    else if (root_ == OPERATOR_PROTOTYPE) {
      result_ = operatorPrototype(builder_, level_ + 1);
    }
    else if (root_ == PARENTHESIZED_EXPRESSION) {
      result_ = parenthesizedExpression(builder_, level_ + 1);
    }
    else if (root_ == PART_OF_STATEMENT) {
      result_ = partOfStatement(builder_, level_ + 1);
    }
    else if (root_ == PATH_OR_LIBRARY_REFERENCE) {
      result_ = pathOrLibraryReference(builder_, level_ + 1);
    }
    else if (root_ == PREFIX_EXPRESSION) {
      result_ = prefixExpression(builder_, level_ + 1);
    }
    else if (root_ == PREFIX_OPERATOR) {
      result_ = prefixOperator(builder_, level_ + 1);
    }
    else if (root_ == QUALIFIED_COMPONENT_NAME) {
      result_ = qualifiedComponentName(builder_, level_ + 1);
    }
    else if (root_ == REDIRECTION) {
      result_ = redirection(builder_, level_ + 1);
    }
    else if (root_ == REFERENCE_EXPRESSION) {
      result_ = referenceExpression(builder_, level_ + 1);
    }
    else if (root_ == RELATIONAL_OPERATOR) {
      result_ = relationalOperator(builder_, level_ + 1);
    }
    else if (root_ == RESOURCE_STATEMENT) {
      result_ = resourceStatement(builder_, level_ + 1);
    }
    else if (root_ == RETURN_STATEMENT) {
      result_ = returnStatement(builder_, level_ + 1);
    }
    else if (root_ == RETURN_TYPE) {
      result_ = returnType(builder_, level_ + 1);
    }
    else if (root_ == SETTER_DECLARATION) {
      result_ = setterDeclaration(builder_, level_ + 1);
    }
    else if (root_ == SHIFT_EXPRESSION) {
      result_ = shiftExpression(builder_, level_ + 1);
    }
    else if (root_ == SHIFT_OPERATOR) {
      result_ = shiftOperator(builder_, level_ + 1);
    }
    else if (root_ == SHIFT_RIGHT_OPERATOR) {
      result_ = shiftRightOperator(builder_, level_ + 1);
    }
    else if (root_ == SHORT_TEMPLATE_ENTRY) {
      result_ = shortTemplateEntry(builder_, level_ + 1);
    }
    else if (root_ == SHOW_COMBINATOR) {
      result_ = showCombinator(builder_, level_ + 1);
    }
    else if (root_ == SOURCE_STATEMENT) {
      result_ = sourceStatement(builder_, level_ + 1);
    }
    else if (root_ == STATEMENTS) {
      result_ = statements(builder_, level_ + 1);
    }
    else if (root_ == STRING_LITERAL_EXPRESSION) {
      result_ = stringLiteralExpression(builder_, level_ + 1);
    }
    else if (root_ == SUFFIX_EXPRESSION) {
      result_ = suffixExpression(builder_, level_ + 1);
    }
    else if (root_ == SUPER_CALL_OR_FIELD_INITIALIZER) {
      result_ = superCallOrFieldInitializer(builder_, level_ + 1);
    }
    else if (root_ == SUPER_EXPRESSION) {
      result_ = superExpression(builder_, level_ + 1);
    }
    else if (root_ == SUPERCLASS) {
      result_ = superclass(builder_, level_ + 1);
    }
    else if (root_ == SUPERINTERFACES) {
      result_ = superinterfaces(builder_, level_ + 1);
    }
    else if (root_ == SWITCH_CASE) {
      result_ = switchCase(builder_, level_ + 1);
    }
    else if (root_ == SWITCH_STATEMENT) {
      result_ = switchStatement(builder_, level_ + 1);
    }
    else if (root_ == TERNARY_EXPRESSION) {
      result_ = ternaryExpression(builder_, level_ + 1);
    }
    else if (root_ == THIS_EXPRESSION) {
      result_ = thisExpression(builder_, level_ + 1);
    }
    else if (root_ == THROW_STATEMENT) {
      result_ = throwStatement(builder_, level_ + 1);
    }
    else if (root_ == TRY_STATEMENT) {
      result_ = tryStatement(builder_, level_ + 1);
    }
    else if (root_ == TYPE) {
      result_ = type(builder_, level_ + 1);
    }
    else if (root_ == TYPE_ARGUMENTS) {
      result_ = typeArguments(builder_, level_ + 1);
    }
    else if (root_ == TYPE_LIST) {
      result_ = typeList(builder_, level_ + 1);
    }
    else if (root_ == TYPE_PARAMETER) {
      result_ = typeParameter(builder_, level_ + 1);
    }
    else if (root_ == TYPE_PARAMETERS) {
      result_ = typeParameters(builder_, level_ + 1);
    }
    else if (root_ == USER_DEFINABLE_OPERATOR) {
      result_ = userDefinableOperator(builder_, level_ + 1);
    }
    else if (root_ == VALUE_EXPRESSION) {
      result_ = valueExpression(builder_, level_ + 1);
    }
    else if (root_ == VAR_ACCESS_DECLARATION) {
      result_ = varAccessDeclaration(builder_, level_ + 1);
    }
    else if (root_ == VAR_DECLARATION) {
      result_ = varDeclaration(builder_, level_ + 1);
    }
    else if (root_ == VAR_DECLARATION_LIST) {
      result_ = varDeclarationList(builder_, level_ + 1);
    }
    else if (root_ == VAR_DECLARATION_LIST_PART) {
      result_ = varDeclarationListPart(builder_, level_ + 1);
    }
    else if (root_ == VAR_INIT) {
      result_ = varInit(builder_, level_ + 1);
    }
    else if (root_ == WHILE_STATEMENT) {
      result_ = whileStatement(builder_, level_ + 1);
    }
    else {
      Marker marker_ = builder_.mark();
      enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, null);
      result_ = parse_root_(root_, builder_, level_);
      exitErrorRecordingSection(builder_, level_, result_, true, _SECTION_RECOVER_, TOKEN_ADVANCER);
      marker_.done(root_);
    }
    return builder_.getTreeBuilt();
  }

  protected boolean parse_root_(final IElementType root_, final PsiBuilder builder_, final int level_) {
    return dartUnit(builder_, level_ + 1);
  }

  private static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    TokenSet.create(ADDITIVE_EXPRESSION, ARRAY_ACCESS_EXPRESSION, ASSIGN_EXPRESSION, AS_EXPRESSION,
      BITWISE_EXPRESSION, CALL_EXPRESSION, CASCADE_REFERENCE_EXPRESSION, COMPARE_EXPRESSION,
      COMPOUND_LITERAL_EXPRESSION, CONST_CONSTRUCTOR_EXPRESSION, EXPRESSION, FUNCTION_EXPRESSION,
      IS_EXPRESSION, ITERATOR_EXPRESSION, LIBRARY_COMPONENT_REFERENCE_EXPRESSION, LIST_LITERAL_EXPRESSION,
      LITERAL_EXPRESSION, LOGIC_AND_EXPRESSION, LOGIC_OR_EXPRESSION, MAP_LITERAL_EXPRESSION,
      MULTIPLICATIVE_EXPRESSION, NEW_EXPRESSION, PARENTHESIZED_EXPRESSION, PREFIX_EXPRESSION,
      REFERENCE_EXPRESSION, SHIFT_EXPRESSION, STRING_LITERAL_EXPRESSION, SUFFIX_EXPRESSION,
      SUPER_EXPRESSION, TERNARY_EXPRESSION, THIS_EXPRESSION, VALUE_EXPRESSION),
  };

  public static boolean type_extends_(IElementType child_, IElementType parent_) {
    for (TokenSet set : EXTENDS_SETS_) {
      if (set.contains(child_) && set.contains(parent_)) return true;
    }
    return false;
  }

  /* ********************************************************** */
  // 'abstract' returnType? 'operator' userDefinableOperator formalParameterList
  public static boolean abstractOperatorDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "abstractOperatorDeclaration")) return false;
    if (!nextTokenIs(builder_, ABSTRACT)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, ABSTRACT);
    result_ = result_ && abstractOperatorDeclaration_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, OPERATOR);
    pinned_ = result_; // pin = 3
    result_ = result_ && report_error_(builder_, userDefinableOperator(builder_, level_ + 1));
    result_ = pinned_ && formalParameterList(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(ABSTRACT_OPERATOR_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // returnType?
  private static boolean abstractOperatorDeclaration_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "abstractOperatorDeclaration_1")) return false;
    returnType(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // abstractOperatorDeclaration ';'
  static boolean abstractOperatorDeclarationWithSemicolon(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "abstractOperatorDeclarationWithSemicolon")) return false;
    if (!nextTokenIs(builder_, ABSTRACT)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = abstractOperatorDeclaration(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // additiveOperator multiplicativeExpressionWrapper
  public static boolean additiveExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additiveExpression")) return false;
    if (!nextTokenIs(builder_, PLUS) && !nextTokenIs(builder_, MINUS)
        && replaceVariants(builder_, 2, "<additive expression>")) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "additiveExpression")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<additive expression>");
    result_ = additiveOperator(builder_, level_ + 1);
    result_ = result_ && multiplicativeExpressionWrapper(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(ADDITIVE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // multiplicativeExpressionWrapper additiveExpression*
  static boolean additiveExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additiveExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = multiplicativeExpressionWrapper(builder_, level_ + 1);
    result_ = result_ && additiveExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // additiveExpression*
  private static boolean additiveExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additiveExpressionWrapper_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!additiveExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "additiveExpressionWrapper_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // '+' | '-'
  public static boolean additiveOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additiveOperator")) return false;
    if (!nextTokenIs(builder_, PLUS) && !nextTokenIs(builder_, MINUS)
        && replaceVariants(builder_, 2, "<additive operator>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<additive operator>");
    result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (result_) {
      marker_.done(ADDITIVE_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // argumentListPart (',' argumentListPart)*
  public static boolean argumentList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argumentList")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<argument list>");
    result_ = argumentListPart(builder_, level_ + 1);
    result_ = result_ && argumentList_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(ARGUMENT_LIST);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, argument_list_recover_parser_);
    return result_;
  }

  // (',' argumentListPart)*
  private static boolean argumentList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argumentList_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!argumentList_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "argumentList_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' argumentListPart
  private static boolean argumentList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argumentList_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argumentListPart(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // namedArgument | expression
  static boolean argumentListPart(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argumentListPart")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, null);
    result_ = namedArgument(builder_, level_ + 1);
    if (!result_) result_ = expression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, argument_list_part_recover_parser_);
    return result_;
  }

  /* ********************************************************** */
  // !(')' | ',')
  static boolean argument_list_part_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_part_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !argument_list_part_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // ')' | ','
  private static boolean argument_list_part_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_part_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, RPAREN);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // !(')')
  static boolean argument_list_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !argument_list_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // (')')
  private static boolean argument_list_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, RPAREN);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '(' argumentList? ')'
  public static boolean arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arguments")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && arguments_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    if (result_) {
      marker_.done(ARGUMENTS);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // argumentList?
  private static boolean arguments_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arguments_1")) return false;
    argumentList(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '[' expression? ']'
  public static boolean arrayAccessExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arrayAccessExpression")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "arrayAccessExpression")) return false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && arrayAccessExpression_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(ARRAY_ACCESS_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // expression?
  private static boolean arrayAccessExpression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arrayAccessExpression_1")) return false;
    expression(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '=>' expression
  static boolean arrowBody(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arrowBody")) return false;
    if (!nextTokenIs(builder_, EXPRESSION_BODY_DEF)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, EXPRESSION_BODY_DEF);
    pinned_ = result_; // pin = 1
    result_ = result_ && expression(builder_, level_ + 1);
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // arrowBody ';'
  static boolean arrowBodyWithSemi(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arrowBodyWithSemi")) return false;
    if (!nextTokenIs(builder_, EXPRESSION_BODY_DEF)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = arrowBody(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'as' type
  public static boolean asExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "asExpression")) return false;
    if (!nextTokenIs(builder_, AS)) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "asExpression")) return false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, AS);
    result_ = result_ && type(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(AS_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'assert' '(' expressionWithRecoverUntilParen ')' ';'
  public static boolean assertStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assertStatement")) return false;
    if (!nextTokenIs(builder_, ASSERT)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, ASSERT);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, consumeToken(builder_, LPAREN));
    result_ = pinned_ && report_error_(builder_, expressionWithRecoverUntilParen(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RPAREN)) && result_;
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (result_ || pinned_) {
      marker_.done(ASSERT_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // assignmentOperator iteratorExpressionWrapper
  public static boolean assignExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignExpression")) return false;
    if (!nextTokenIs(builder_, REM_EQ) && !nextTokenIs(builder_, AND_EQ)
        && !nextTokenIs(builder_, MUL_EQ) && !nextTokenIs(builder_, PLUS_EQ)
        && !nextTokenIs(builder_, MINUS_EQ) && !nextTokenIs(builder_, DIV_EQ)
        && !nextTokenIs(builder_, LT_LT_EQ) && !nextTokenIs(builder_, EQ)
        && !nextTokenIs(builder_, GT_GT_EQ) && !nextTokenIs(builder_, GT_GT_GT_EQ)
        && !nextTokenIs(builder_, XOR_EQ) && !nextTokenIs(builder_, OR_EQ)
        && !nextTokenIs(builder_, INT_DIV_EQ) && replaceVariants(builder_, 13, "<assign expression>")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "assignExpression")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<assign expression>");
    result_ = assignmentOperator(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && iteratorExpressionWrapper(builder_, level_ + 1);
    if (result_ || pinned_) {
      marker_.drop();
      left_marker_.precede().done(ASSIGN_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // iteratorExpressionWrapper assignExpression*
  static boolean assignExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = iteratorExpressionWrapper(builder_, level_ + 1);
    result_ = result_ && assignExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // assignExpression*
  private static boolean assignExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignExpressionWrapper_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!assignExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "assignExpressionWrapper_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // '=' | '*=' | '/=' | '~/=' | '%=' | '+=' | '-=' | '<<=' | '>>>=' | '>>=' | '&=' | '^=' | '|='
  public static boolean assignmentOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignmentOperator")) return false;
    if (!nextTokenIs(builder_, REM_EQ) && !nextTokenIs(builder_, AND_EQ)
        && !nextTokenIs(builder_, MUL_EQ) && !nextTokenIs(builder_, PLUS_EQ)
        && !nextTokenIs(builder_, MINUS_EQ) && !nextTokenIs(builder_, DIV_EQ)
        && !nextTokenIs(builder_, LT_LT_EQ) && !nextTokenIs(builder_, EQ)
        && !nextTokenIs(builder_, GT_GT_EQ) && !nextTokenIs(builder_, GT_GT_GT_EQ)
        && !nextTokenIs(builder_, XOR_EQ) && !nextTokenIs(builder_, OR_EQ)
        && !nextTokenIs(builder_, INT_DIV_EQ) && replaceVariants(builder_, 13, "<assignment operator>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<assignment operator>");
    result_ = consumeToken(builder_, EQ);
    if (!result_) result_ = consumeToken(builder_, MUL_EQ);
    if (!result_) result_ = consumeToken(builder_, DIV_EQ);
    if (!result_) result_ = consumeToken(builder_, INT_DIV_EQ);
    if (!result_) result_ = consumeToken(builder_, REM_EQ);
    if (!result_) result_ = consumeToken(builder_, PLUS_EQ);
    if (!result_) result_ = consumeToken(builder_, MINUS_EQ);
    if (!result_) result_ = consumeToken(builder_, LT_LT_EQ);
    if (!result_) result_ = consumeToken(builder_, GT_GT_GT_EQ);
    if (!result_) result_ = consumeToken(builder_, GT_GT_EQ);
    if (!result_) result_ = consumeToken(builder_, AND_EQ);
    if (!result_) result_ = consumeToken(builder_, XOR_EQ);
    if (!result_) result_ = consumeToken(builder_, OR_EQ);
    if (result_) {
      marker_.done(ASSIGNMENT_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // bitwiseOperator shiftExpressionWrapper
  public static boolean bitwiseExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitwiseExpression")) return false;
    if (!nextTokenIs(builder_, AND) && !nextTokenIs(builder_, XOR)
        && !nextTokenIs(builder_, OR) && replaceVariants(builder_, 3, "<bitwise expression>")) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "bitwiseExpression")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<bitwise expression>");
    result_ = bitwiseOperator(builder_, level_ + 1);
    result_ = result_ && shiftExpressionWrapper(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(BITWISE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // shiftExpressionWrapper bitwiseExpression*
  static boolean bitwiseExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitwiseExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = shiftExpressionWrapper(builder_, level_ + 1);
    result_ = result_ && bitwiseExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // bitwiseExpression*
  private static boolean bitwiseExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitwiseExpressionWrapper_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!bitwiseExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "bitwiseExpressionWrapper_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // '&' | '^' | '|'
  public static boolean bitwiseOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitwiseOperator")) return false;
    if (!nextTokenIs(builder_, AND) && !nextTokenIs(builder_, XOR)
        && !nextTokenIs(builder_, OR) && replaceVariants(builder_, 3, "<bitwise operator>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<bitwise operator>");
    result_ = consumeToken(builder_, AND);
    if (!result_) result_ = consumeToken(builder_, XOR);
    if (!result_) result_ = consumeToken(builder_, OR);
    if (result_) {
      marker_.done(BITWISE_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // '{' statements '}'
  public static boolean block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, LBRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, statements(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RBRACE) && result_;
    if (result_ || pinned_) {
      marker_.done(BLOCK);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'break' referenceExpression? ';'
  public static boolean breakStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "breakStatement")) return false;
    if (!nextTokenIs(builder_, BREAK)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, BREAK);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, breakStatement_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (result_ || pinned_) {
      marker_.done(BREAK_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // referenceExpression?
  private static boolean breakStatement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "breakStatement_1")) return false;
    referenceExpression(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // arguments
  public static boolean callExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "callExpression")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "callExpression")) return false;
    Marker marker_ = builder_.mark();
    result_ = arguments(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(CALL_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // (referenceExpression | thisExpression | superExpression | parenthesizedExpression) (callExpression | arrayAccessExpression | qualifiedReferenceExpression)*
  static boolean callOrArrayAccess(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "callOrArrayAccess")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = callOrArrayAccess_0(builder_, level_ + 1);
    result_ = result_ && callOrArrayAccess_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // referenceExpression | thisExpression | superExpression | parenthesizedExpression
  private static boolean callOrArrayAccess_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "callOrArrayAccess_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = referenceExpression(builder_, level_ + 1);
    if (!result_) result_ = thisExpression(builder_, level_ + 1);
    if (!result_) result_ = superExpression(builder_, level_ + 1);
    if (!result_) result_ = parenthesizedExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (callExpression | arrayAccessExpression | qualifiedReferenceExpression)*
  private static boolean callOrArrayAccess_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "callOrArrayAccess_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!callOrArrayAccess_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "callOrArrayAccess_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // callExpression | arrayAccessExpression | qualifiedReferenceExpression
  private static boolean callOrArrayAccess_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "callOrArrayAccess_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = callExpression(builder_, level_ + 1);
    if (!result_) result_ = arrayAccessExpression(builder_, level_ + 1);
    if (!result_) result_ = qualifiedReferenceExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '.''.' << cascadeStopper >> callOrArrayAccess << varInitWrapper >>
  public static boolean cascadeReferenceExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cascadeReferenceExpression")) return false;
    if (!nextTokenIs(builder_, DOT)) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "cascadeReferenceExpression")) return false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && consumeToken(builder_, DOT);
    result_ = result_ && cascadeStopper(builder_, level_ + 1);
    result_ = result_ && callOrArrayAccess(builder_, level_ + 1);
    result_ = result_ && varInitWrapper(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(CASCADE_REFERENCE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'catch' formalParameterList block
  public static boolean catchPart(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catchPart")) return false;
    if (!nextTokenIs(builder_, CATCH)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, CATCH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, formalParameterList(builder_, level_ + 1));
    result_ = pinned_ && block(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(CATCH_PART);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // (metadata* classMemberDefinition)*
  public static boolean classBody(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classBody")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<class body>");
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!classBody_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "classBody");
        break;
      }
      offset_ = next_offset_;
    }
    marker_.done(CLASS_BODY);
    exitErrorRecordingSection(builder_, level_, true, false, _SECTION_RECOVER_, simple_scope_recover_parser_);
    return true;
  }

  // metadata* classMemberDefinition
  private static boolean classBody_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classBody_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = classBody_0_0(builder_, level_ + 1);
    result_ = result_ && classMemberDefinition(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // metadata*
  private static boolean classBody_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classBody_0_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!metadata(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "classBody_0_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // '{' classBody '}'
  static boolean classBodyWithBracesPrivate(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classBodyWithBracesPrivate")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && classBody(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'abstract'? 'class' componentName typeParameters? (superclass mixins?)? interfaces? ('native' stringLiteralExpression?)? classBodyWithBracesPrivate
  public static boolean classDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition")) return false;
    if (!nextTokenIs(builder_, ABSTRACT) && !nextTokenIs(builder_, CLASS)
        && replaceVariants(builder_, 2, "<class definition>")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<class definition>");
    result_ = classDefinition_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, CLASS);
    result_ = result_ && componentName(builder_, level_ + 1);
    pinned_ = result_; // pin = 3
    result_ = result_ && report_error_(builder_, classDefinition_3(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, classDefinition_4(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, classDefinition_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, classDefinition_6(builder_, level_ + 1)) && result_;
    result_ = pinned_ && classBodyWithBracesPrivate(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(CLASS_DEFINITION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // 'abstract'?
  private static boolean classDefinition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition_0")) return false;
    consumeToken(builder_, ABSTRACT);
    return true;
  }

  // typeParameters?
  private static boolean classDefinition_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition_3")) return false;
    typeParameters(builder_, level_ + 1);
    return true;
  }

  // (superclass mixins?)?
  private static boolean classDefinition_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition_4")) return false;
    classDefinition_4_0(builder_, level_ + 1);
    return true;
  }

  // superclass mixins?
  private static boolean classDefinition_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition_4_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = superclass(builder_, level_ + 1);
    result_ = result_ && classDefinition_4_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // mixins?
  private static boolean classDefinition_4_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition_4_0_1")) return false;
    mixins(builder_, level_ + 1);
    return true;
  }

  // interfaces?
  private static boolean classDefinition_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition_5")) return false;
    interfaces(builder_, level_ + 1);
    return true;
  }

  // ('native' stringLiteralExpression?)?
  private static boolean classDefinition_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition_6")) return false;
    classDefinition_6_0(builder_, level_ + 1);
    return true;
  }

  // 'native' stringLiteralExpression?
  private static boolean classDefinition_6_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition_6_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, NATIVE);
    result_ = result_ && classDefinition_6_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // stringLiteralExpression?
  private static boolean classDefinition_6_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classDefinition_6_0_1")) return false;
    stringLiteralExpression(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // factoryConstructorDeclaration
  //                                 | abstractOperatorDeclarationWithSemicolon
  //                                 | namedConstructorDeclaration
  //                                 | methodDeclaration
  //                                 | operatorDeclaration
  //                                 | getterOrSetterDeclaration
  //                                 | varDeclarationListWithSemicolon
  static boolean classMemberDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "classMemberDefinition")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, null);
    result_ = factoryConstructorDeclaration(builder_, level_ + 1);
    if (!result_) result_ = abstractOperatorDeclarationWithSemicolon(builder_, level_ + 1);
    if (!result_) result_ = namedConstructorDeclaration(builder_, level_ + 1);
    if (!result_) result_ = methodDeclaration(builder_, level_ + 1);
    if (!result_) result_ = operatorDeclaration(builder_, level_ + 1);
    if (!result_) result_ = getterOrSetterDeclaration(builder_, level_ + 1);
    if (!result_) result_ = varDeclarationListWithSemicolon(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, class_member_recover_parser_);
    return result_;
  }

  /* ********************************************************** */
  // !('abstract' | 'const' | 'factory' | 'final' | 'get' | 'operator'
  //                                  | 'set' | 'static' | 'var' | <<nonStrictID>> | '}' | '@' | 'external')
  static boolean class_member_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_member_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !class_member_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // 'abstract' | 'const' | 'factory' | 'final' | 'get' | 'operator'
  //                                  | 'set' | 'static' | 'var' | <<nonStrictID>> | '}' | '@' | 'external'
  private static boolean class_member_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_member_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, ABSTRACT);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) result_ = consumeToken(builder_, FACTORY);
    if (!result_) result_ = consumeToken(builder_, FINAL);
    if (!result_) result_ = consumeToken(builder_, GET);
    if (!result_) result_ = consumeToken(builder_, OPERATOR);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, VAR);
    if (!result_) result_ = nonStrictID(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    if (!result_) result_ = consumeToken(builder_, AT);
    if (!result_) result_ = consumeToken(builder_, EXTERNAL);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // showCombinator | hideCombinator
  static boolean combinator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "combinator")) return false;
    if (!nextTokenIs(builder_, HIDE) && !nextTokenIs(builder_, SHOW)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = showCombinator(builder_, level_ + 1);
    if (!result_) result_ = hideCombinator(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // (relationalOperator | equalityOperator) bitwiseExpressionWrapper
  public static boolean compareExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compareExpression")) return false;
    if (!nextTokenIs(builder_, NEQ) && !nextTokenIs(builder_, NEQ_EQ)
        && !nextTokenIs(builder_, LT) && !nextTokenIs(builder_, LT_EQ)
        && !nextTokenIs(builder_, EQ_EQ) && !nextTokenIs(builder_, EQ_EQ_EQ)
        && !nextTokenIs(builder_, GT) && !nextTokenIs(builder_, GT_EQ)
        && replaceVariants(builder_, 8, "<compare expression>")) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "compareExpression")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<compare expression>");
    result_ = compareExpression_0(builder_, level_ + 1);
    result_ = result_ && bitwiseExpressionWrapper(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(COMPARE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // relationalOperator | equalityOperator
  private static boolean compareExpression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compareExpression_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = relationalOperator(builder_, level_ + 1);
    if (!result_) result_ = equalityOperator(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // bitwiseExpressionWrapper compareExpression*
  static boolean compareExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compareExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = bitwiseExpressionWrapper(builder_, level_ + 1);
    result_ = result_ && compareExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // compareExpression*
  private static boolean compareExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compareExpressionWrapper_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!compareExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "compareExpressionWrapper_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // << nonStrictID >>
  public static boolean componentName(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentName")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<component name>");
    result_ = nonStrictID(builder_, level_ + 1);
    if (result_) {
      marker_.done(COMPONENT_NAME);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // 'const'? typeArguments? (listLiteralExpression | mapLiteralExpression)
  public static boolean compoundLiteralExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compoundLiteralExpression")) return false;
    if (!nextTokenIs(builder_, LT) && !nextTokenIs(builder_, LBRACKET)
        && !nextTokenIs(builder_, CONST) && !nextTokenIs(builder_, LBRACE)
        && replaceVariants(builder_, 4, "<compound literal expression>")) return false;
    boolean result_ = false;
    int start_ = builder_.getCurrentOffset();
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<compound literal expression>");
    result_ = compoundLiteralExpression_0(builder_, level_ + 1);
    result_ = result_ && compoundLiteralExpression_1(builder_, level_ + 1);
    result_ = result_ && compoundLiteralExpression_2(builder_, level_ + 1);
    LighterASTNode last_ = result_? builder_.getLatestDoneMarker() : null;
    if (last_ != null && last_.getStartOffset() == start_ && type_extends_(last_.getTokenType(), COMPOUND_LITERAL_EXPRESSION)) {
      marker_.drop();
    }
    else if (result_) {
      marker_.done(COMPOUND_LITERAL_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // 'const'?
  private static boolean compoundLiteralExpression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compoundLiteralExpression_0")) return false;
    consumeToken(builder_, CONST);
    return true;
  }

  // typeArguments?
  private static boolean compoundLiteralExpression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compoundLiteralExpression_1")) return false;
    typeArguments(builder_, level_ + 1);
    return true;
  }

  // listLiteralExpression | mapLiteralExpression
  private static boolean compoundLiteralExpression_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compoundLiteralExpression_2")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = listLiteralExpression(builder_, level_ + 1);
    if (!result_) result_ = mapLiteralExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'const' type ('.' referenceExpression)? arguments
  public static boolean constConstructorExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constConstructorExpression")) return false;
    if (!nextTokenIs(builder_, CONST)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, CONST);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, type(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, constConstructorExpression_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && arguments(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(CONST_CONSTRUCTOR_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('.' referenceExpression)?
  private static boolean constConstructorExpression_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constConstructorExpression_2")) return false;
    constConstructorExpression_2_0(builder_, level_ + 1);
    return true;
  }

  // '.' referenceExpression
  private static boolean constConstructorExpression_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constConstructorExpression_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && referenceExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'continue' referenceExpression? ';'
  public static boolean continueStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continueStatement")) return false;
    if (!nextTokenIs(builder_, CONTINUE)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, CONTINUE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, continueStatement_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (result_ || pinned_) {
      marker_.done(CONTINUE_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // referenceExpression?
  private static boolean continueStatement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continueStatement_1")) return false;
    referenceExpression(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // (metadata* topLevelDefinition)*
  static boolean dartUnit(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dartUnit")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!dartUnit_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "dartUnit");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // metadata* topLevelDefinition
  private static boolean dartUnit_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dartUnit_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = dartUnit_0_0(builder_, level_ + 1);
    result_ = result_ && topLevelDefinition(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // metadata*
  private static boolean dartUnit_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dartUnit_0_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!metadata(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "dartUnit_0_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // label? ('case' expression ':')* 'default' ':' statements
  public static boolean defaultCase(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultCase")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<default case>");
    result_ = defaultCase_0(builder_, level_ + 1);
    result_ = result_ && defaultCase_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, DEFAULT);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && statements(builder_, level_ + 1);
    if (result_) {
      marker_.done(DEFAULT_CASE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // label?
  private static boolean defaultCase_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultCase_0")) return false;
    label(builder_, level_ + 1);
    return true;
  }

  // ('case' expression ':')*
  private static boolean defaultCase_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultCase_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!defaultCase_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "defaultCase_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // 'case' expression ':'
  private static boolean defaultCase_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultCase_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, CASE);
    result_ = result_ && expression(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'default' simpleQualifiedReferenceExpression typeParameters?
  public static boolean defaultFactroy(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultFactroy")) return false;
    if (!nextTokenIs(builder_, DEFAULT)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, DEFAULT);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, simpleQualifiedReferenceExpression(builder_, level_ + 1));
    result_ = pinned_ && defaultFactroy_2(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(DEFAULT_FACTROY);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // typeParameters?
  private static boolean defaultFactroy_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultFactroy_2")) return false;
    typeParameters(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // normalFormalParameter (('=' | ':') expression)?
  public static boolean defaultFormalNamedParameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultFormalNamedParameter")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<default formal named parameter>");
    result_ = normalFormalParameter(builder_, level_ + 1);
    result_ = result_ && defaultFormalNamedParameter_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(DEFAULT_FORMAL_NAMED_PARAMETER);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, default_formal_parameter_recover_parser_);
    return result_;
  }

  // (('=' | ':') expression)?
  private static boolean defaultFormalNamedParameter_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultFormalNamedParameter_1")) return false;
    defaultFormalNamedParameter_1_0(builder_, level_ + 1);
    return true;
  }

  // ('=' | ':') expression
  private static boolean defaultFormalNamedParameter_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultFormalNamedParameter_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = defaultFormalNamedParameter_1_0_0(builder_, level_ + 1);
    result_ = result_ && expression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // '=' | ':'
  private static boolean defaultFormalNamedParameter_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defaultFormalNamedParameter_1_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, EQ);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // !(')' | ',' | ']' | '}')
  static boolean default_formal_parameter_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_formal_parameter_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !default_formal_parameter_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // ')' | ',' | ']' | '}'
  private static boolean default_formal_parameter_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_formal_parameter_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, RPAREN);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, RBRACKET);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'do' statement 'while' '(' expressionWithRecoverUntilParen ')' ';'
  public static boolean doWhileStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "doWhileStatement")) return false;
    if (!nextTokenIs(builder_, DO)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, DO);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, statement(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, WHILE)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, LPAREN)) && result_;
    result_ = pinned_ && report_error_(builder_, expressionWithRecoverUntilParen(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RPAREN)) && result_;
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (result_ || pinned_) {
      marker_.done(DO_WHILE_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '==' | '!=' | '===' | '!=='
  public static boolean equalityOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "equalityOperator")) return false;
    if (!nextTokenIs(builder_, NEQ) && !nextTokenIs(builder_, NEQ_EQ)
        && !nextTokenIs(builder_, EQ_EQ) && !nextTokenIs(builder_, EQ_EQ_EQ)
        && replaceVariants(builder_, 4, "<equality operator>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<equality operator>");
    result_ = consumeToken(builder_, EQ_EQ);
    if (!result_) result_ = consumeToken(builder_, NEQ);
    if (!result_) result_ = consumeToken(builder_, EQ_EQ_EQ);
    if (!result_) result_ = consumeToken(builder_, NEQ_EQ);
    if (result_) {
      marker_.done(EQUALITY_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // 'export' pathOrLibraryReference ('as' componentName )? combinator* ';'
  public static boolean exportStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exportStatement")) return false;
    if (!nextTokenIs(builder_, EXPORT)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, EXPORT);
    result_ = result_ && pathOrLibraryReference(builder_, level_ + 1);
    result_ = result_ && exportStatement_2(builder_, level_ + 1);
    result_ = result_ && exportStatement_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (result_) {
      marker_.done(EXPORT_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // ('as' componentName )?
  private static boolean exportStatement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exportStatement_2")) return false;
    exportStatement_2_0(builder_, level_ + 1);
    return true;
  }

  // 'as' componentName
  private static boolean exportStatement_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exportStatement_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, AS);
    result_ = result_ && componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // combinator*
  private static boolean exportStatement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exportStatement_3")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!combinator(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "exportStatement_3");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // assignExpressionWrapper
  public static boolean expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression")) return false;
    boolean result_ = false;
    int start_ = builder_.getCurrentOffset();
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<expression>");
    result_ = assignExpressionWrapper(builder_, level_ + 1);
    LighterASTNode last_ = result_? builder_.getLatestDoneMarker() : null;
    if (last_ != null && last_.getStartOffset() == start_ && type_extends_(last_.getTokenType(), EXPRESSION)) {
      marker_.drop();
    }
    else if (result_) {
      marker_.done(EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, expression_recover_parser_);
    return result_;
  }

  /* ********************************************************** */
  // expression (',' expression)*
  public static boolean expressionList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionList")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<expression list>");
    result_ = expression(builder_, level_ + 1);
    result_ = result_ && expressionList_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(EXPRESSION_LIST);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // (',' expression)*
  private static boolean expressionList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionList_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!expressionList_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "expressionList_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' expression
  private static boolean expressionList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionList_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && expression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // expression
  static boolean expressionWithRecoverUntilParen(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionWithRecoverUntilParen")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, null);
    result_ = expression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, not_paren_recover_parser_);
    return result_;
  }

  /* ********************************************************** */
  // !('!' | '!=' | '!==' | 'is'| '%' | '%=' | '&&' | '&' | '&=' | '(' | ')' | '*' | '*=' | '+' | '++'
  //                                | '+=' | ',' | '-' | '--' | '-=' | '...' | '/' | '/=' | ':' | ';' | '<' | '<<' | '<<=' | '<='
  //                                | '=' | '==' | '===' | '=>' | '>' | '>=' | '>>=' | '>>>=' | '?' | '[' | ']'
  //                                | '^' | '^=' | 'abstract' | 'assert' | 'break' | 'case' | 'catch' | 'class' | 'const'
  //                                | 'continue' | 'default' | 'do' | 'else' | 'factory' | 'false' | 'final' | 'finally'
  //                                | 'for' | 'get' | 'if' | 'in' | 'interface' | 'native' | 'new' | 'null' | 'operator'
  //                                | 'return' | 'set' | 'static' | 'super' | 'switch' | 'this' | 'throw' | 'true' | 'try'
  //                                | 'typedef' | 'var' | 'while' | '{' | '|' | '|=' | '||' | '}' | '~' | '~/=' | '.'
  //                                | HEX_NUMBER | <<nonStrictID>> | NUMBER | OPEN_QUOTE | RAW_SINGLE_QUOTED_STRING | RAW_TRIPLE_QUOTED_STRING
  //                                | LONG_TEMPLATE_ENTRY_END | shiftRightOperator | ('.' '.'))
  static boolean expression_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !expression_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // '!' | '!=' | '!==' | 'is'| '%' | '%=' | '&&' | '&' | '&=' | '(' | ')' | '*' | '*=' | '+' | '++'
  //                                | '+=' | ',' | '-' | '--' | '-=' | '...' | '/' | '/=' | ':' | ';' | '<' | '<<' | '<<=' | '<='
  //                                | '=' | '==' | '===' | '=>' | '>' | '>=' | '>>=' | '>>>=' | '?' | '[' | ']'
  //                                | '^' | '^=' | 'abstract' | 'assert' | 'break' | 'case' | 'catch' | 'class' | 'const'
  //                                | 'continue' | 'default' | 'do' | 'else' | 'factory' | 'false' | 'final' | 'finally'
  //                                | 'for' | 'get' | 'if' | 'in' | 'interface' | 'native' | 'new' | 'null' | 'operator'
  //                                | 'return' | 'set' | 'static' | 'super' | 'switch' | 'this' | 'throw' | 'true' | 'try'
  //                                | 'typedef' | 'var' | 'while' | '{' | '|' | '|=' | '||' | '}' | '~' | '~/=' | '.'
  //                                | HEX_NUMBER | <<nonStrictID>> | NUMBER | OPEN_QUOTE | RAW_SINGLE_QUOTED_STRING | RAW_TRIPLE_QUOTED_STRING
  //                                | LONG_TEMPLATE_ENTRY_END | shiftRightOperator | ('.' '.')
  private static boolean expression_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, NOT);
    if (!result_) result_ = consumeToken(builder_, NEQ);
    if (!result_) result_ = consumeToken(builder_, NEQ_EQ);
    if (!result_) result_ = consumeToken(builder_, IS);
    if (!result_) result_ = consumeToken(builder_, REM);
    if (!result_) result_ = consumeToken(builder_, REM_EQ);
    if (!result_) result_ = consumeToken(builder_, AND_AND);
    if (!result_) result_ = consumeToken(builder_, AND);
    if (!result_) result_ = consumeToken(builder_, AND_EQ);
    if (!result_) result_ = consumeToken(builder_, LPAREN);
    if (!result_) result_ = consumeToken(builder_, RPAREN);
    if (!result_) result_ = consumeToken(builder_, MUL);
    if (!result_) result_ = consumeToken(builder_, MUL_EQ);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, PLUS_PLUS);
    if (!result_) result_ = consumeToken(builder_, PLUS_EQ);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, MINUS_MINUS);
    if (!result_) result_ = consumeToken(builder_, MINUS_EQ);
    if (!result_) result_ = consumeToken(builder_, "...");
    if (!result_) result_ = consumeToken(builder_, DIV);
    if (!result_) result_ = consumeToken(builder_, DIV_EQ);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = consumeToken(builder_, LT);
    if (!result_) result_ = consumeToken(builder_, LT_LT);
    if (!result_) result_ = consumeToken(builder_, LT_LT_EQ);
    if (!result_) result_ = consumeToken(builder_, LT_EQ);
    if (!result_) result_ = consumeToken(builder_, EQ);
    if (!result_) result_ = consumeToken(builder_, EQ_EQ);
    if (!result_) result_ = consumeToken(builder_, EQ_EQ_EQ);
    if (!result_) result_ = consumeToken(builder_, EXPRESSION_BODY_DEF);
    if (!result_) result_ = consumeToken(builder_, GT);
    if (!result_) result_ = consumeToken(builder_, GT_EQ);
    if (!result_) result_ = consumeToken(builder_, GT_GT_EQ);
    if (!result_) result_ = consumeToken(builder_, GT_GT_GT_EQ);
    if (!result_) result_ = consumeToken(builder_, QUEST);
    if (!result_) result_ = consumeToken(builder_, LBRACKET);
    if (!result_) result_ = consumeToken(builder_, RBRACKET);
    if (!result_) result_ = consumeToken(builder_, XOR);
    if (!result_) result_ = consumeToken(builder_, XOR_EQ);
    if (!result_) result_ = consumeToken(builder_, ABSTRACT);
    if (!result_) result_ = consumeToken(builder_, ASSERT);
    if (!result_) result_ = consumeToken(builder_, BREAK);
    if (!result_) result_ = consumeToken(builder_, CASE);
    if (!result_) result_ = consumeToken(builder_, CATCH);
    if (!result_) result_ = consumeToken(builder_, CLASS);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) result_ = consumeToken(builder_, CONTINUE);
    if (!result_) result_ = consumeToken(builder_, DEFAULT);
    if (!result_) result_ = consumeToken(builder_, DO);
    if (!result_) result_ = consumeToken(builder_, ELSE);
    if (!result_) result_ = consumeToken(builder_, FACTORY);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    if (!result_) result_ = consumeToken(builder_, FINAL);
    if (!result_) result_ = consumeToken(builder_, FINALLY);
    if (!result_) result_ = consumeToken(builder_, FOR);
    if (!result_) result_ = consumeToken(builder_, GET);
    if (!result_) result_ = consumeToken(builder_, IF);
    if (!result_) result_ = consumeToken(builder_, IN);
    if (!result_) result_ = consumeToken(builder_, INTERFACE);
    if (!result_) result_ = consumeToken(builder_, NATIVE);
    if (!result_) result_ = consumeToken(builder_, NEW);
    if (!result_) result_ = consumeToken(builder_, NULL);
    if (!result_) result_ = consumeToken(builder_, OPERATOR);
    if (!result_) result_ = consumeToken(builder_, RETURN);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, SUPER);
    if (!result_) result_ = consumeToken(builder_, SWITCH);
    if (!result_) result_ = consumeToken(builder_, THIS);
    if (!result_) result_ = consumeToken(builder_, THROW);
    if (!result_) result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, TRY);
    if (!result_) result_ = consumeToken(builder_, TYPEDEF);
    if (!result_) result_ = consumeToken(builder_, VAR);
    if (!result_) result_ = consumeToken(builder_, WHILE);
    if (!result_) result_ = consumeToken(builder_, LBRACE);
    if (!result_) result_ = consumeToken(builder_, OR);
    if (!result_) result_ = consumeToken(builder_, OR_EQ);
    if (!result_) result_ = consumeToken(builder_, OR_OR);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    if (!result_) result_ = consumeToken(builder_, BIN_NOT);
    if (!result_) result_ = consumeToken(builder_, INT_DIV_EQ);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, HEX_NUMBER);
    if (!result_) result_ = nonStrictID(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, OPEN_QUOTE);
    if (!result_) result_ = consumeToken(builder_, RAW_SINGLE_QUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, RAW_TRIPLE_QUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, LONG_TEMPLATE_ENTRY_END);
    if (!result_) result_ = shiftRightOperator(builder_, level_ + 1);
    if (!result_) result_ = expression_recover_0_94(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // '.' '.'
  private static boolean expression_recover_0_94(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_recover_0_94")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && consumeToken(builder_, DOT);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // ('external' | 'const')* 'factory' referenceExpression ('.' componentName)? formalParameterList (varInit ';' | functionBodyOrNative | ';')?
  public static boolean factoryConstructorDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factoryConstructorDeclaration")) return false;
    if (!nextTokenIs(builder_, CONST) && !nextTokenIs(builder_, EXTERNAL)
        && !nextTokenIs(builder_, FACTORY) && replaceVariants(builder_, 3, "<factory constructor declaration>")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<factory constructor declaration>");
    result_ = factoryConstructorDeclaration_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, FACTORY);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, referenceExpression(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, factoryConstructorDeclaration_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, formalParameterList(builder_, level_ + 1)) && result_;
    result_ = pinned_ && factoryConstructorDeclaration_5(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(FACTORY_CONSTRUCTOR_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('external' | 'const')*
  private static boolean factoryConstructorDeclaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factoryConstructorDeclaration_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!factoryConstructorDeclaration_0_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "factoryConstructorDeclaration_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // 'external' | 'const'
  private static boolean factoryConstructorDeclaration_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factoryConstructorDeclaration_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, EXTERNAL);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ('.' componentName)?
  private static boolean factoryConstructorDeclaration_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factoryConstructorDeclaration_3")) return false;
    factoryConstructorDeclaration_3_0(builder_, level_ + 1);
    return true;
  }

  // '.' componentName
  private static boolean factoryConstructorDeclaration_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factoryConstructorDeclaration_3_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (varInit ';' | functionBodyOrNative | ';')?
  private static boolean factoryConstructorDeclaration_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factoryConstructorDeclaration_5")) return false;
    factoryConstructorDeclaration_5_0(builder_, level_ + 1);
    return true;
  }

  // varInit ';' | functionBodyOrNative | ';'
  private static boolean factoryConstructorDeclaration_5_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factoryConstructorDeclaration_5_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = factoryConstructorDeclaration_5_0_0(builder_, level_ + 1);
    if (!result_) result_ = functionBodyOrNative(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // varInit ';'
  private static boolean factoryConstructorDeclaration_5_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factoryConstructorDeclaration_5_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = varInit(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'factory' type
  public static boolean factorySpecification(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factorySpecification")) return false;
    if (!nextTokenIs(builder_, FACTORY)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, FACTORY);
    pinned_ = result_; // pin = 1
    result_ = result_ && type(builder_, level_ + 1);
    if (result_ || pinned_) {
      marker_.done(FACTORY_SPECIFICATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // finalVarOrType? 'this' '.' referenceExpression
  public static boolean fieldFormalParameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldFormalParameter")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<field formal parameter>");
    result_ = fieldFormalParameter_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, THIS);
    result_ = result_ && consumeToken(builder_, DOT);
    result_ = result_ && referenceExpression(builder_, level_ + 1);
    if (result_) {
      marker_.done(FIELD_FORMAL_PARAMETER);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // finalVarOrType?
  private static boolean fieldFormalParameter_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldFormalParameter_0")) return false;
    finalVarOrType(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ('this' '.')? referenceExpression '=' expression
  public static boolean fieldInitializer(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldInitializer")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<field initializer>");
    result_ = fieldInitializer_0(builder_, level_ + 1);
    result_ = result_ && referenceExpression(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, consumeToken(builder_, EQ));
    result_ = pinned_ && expression(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(FIELD_INITIALIZER);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('this' '.')?
  private static boolean fieldInitializer_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldInitializer_0")) return false;
    fieldInitializer_0_0(builder_, level_ + 1);
    return true;
  }

  // 'this' '.'
  private static boolean fieldInitializer_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldInitializer_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, THIS);
    result_ = result_ && consumeToken(builder_, DOT);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'final' | 'const'
  static boolean finalOrConst(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "finalOrConst")) return false;
    if (!nextTokenIs(builder_, CONST) && !nextTokenIs(builder_, FINAL)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, FINAL);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'final' type? | 'var' | type
  public static boolean finalVarOrType(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "finalVarOrType")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<final var or type>");
    result_ = finalVarOrType_0(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, VAR);
    if (!result_) result_ = type(builder_, level_ + 1);
    if (result_) {
      marker_.done(FINAL_VAR_OR_TYPE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // 'final' type?
  private static boolean finalVarOrType_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "finalVarOrType_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, FINAL);
    result_ = result_ && finalVarOrType_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // type?
  private static boolean finalVarOrType_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "finalVarOrType_0_1")) return false;
    type(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'finally' block
  public static boolean finallyPart(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "finallyPart")) return false;
    if (!nextTokenIs(builder_, FINALLY)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, FINALLY);
    pinned_ = result_; // pin = 1
    result_ = result_ && block(builder_, level_ + 1);
    if (result_ || pinned_) {
      marker_.done(FINALLY_PART);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // (varAccessDeclaration | componentName) 'in' expression
  public static boolean forInPart(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forInPart")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<for in part>");
    result_ = forInPart_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, IN);
    result_ = result_ && expression(builder_, level_ + 1);
    if (result_) {
      marker_.done(FOR_IN_PART);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // varAccessDeclaration | componentName
  private static boolean forInPart_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forInPart_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = varAccessDeclaration(builder_, level_ + 1);
    if (!result_) result_ = componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // varDeclarationList ';' expression? ';' expressionList?
  //               | forInPart
  //               | expressionList? ';' expression? ';' expressionList?
  public static boolean forLoopParts(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forLoopParts")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<for loop parts>");
    result_ = forLoopParts_0(builder_, level_ + 1);
    if (!result_) result_ = forInPart(builder_, level_ + 1);
    if (!result_) result_ = forLoopParts_2(builder_, level_ + 1);
    if (result_) {
      marker_.done(FOR_LOOP_PARTS);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, for_loops_parts_recover_parser_);
    return result_;
  }

  // varDeclarationList ';' expression? ';' expressionList?
  private static boolean forLoopParts_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forLoopParts_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = varDeclarationList(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    result_ = result_ && forLoopParts_0_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    result_ = result_ && forLoopParts_0_4(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // expression?
  private static boolean forLoopParts_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forLoopParts_0_2")) return false;
    expression(builder_, level_ + 1);
    return true;
  }

  // expressionList?
  private static boolean forLoopParts_0_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forLoopParts_0_4")) return false;
    expressionList(builder_, level_ + 1);
    return true;
  }

  // expressionList? ';' expression? ';' expressionList?
  private static boolean forLoopParts_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forLoopParts_2")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = forLoopParts_2_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    result_ = result_ && forLoopParts_2_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    result_ = result_ && forLoopParts_2_4(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // expressionList?
  private static boolean forLoopParts_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forLoopParts_2_0")) return false;
    expressionList(builder_, level_ + 1);
    return true;
  }

  // expression?
  private static boolean forLoopParts_2_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forLoopParts_2_2")) return false;
    expression(builder_, level_ + 1);
    return true;
  }

  // expressionList?
  private static boolean forLoopParts_2_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forLoopParts_2_4")) return false;
    expressionList(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '(' forLoopParts ')'
  public static boolean forLoopPartsInBraces(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forLoopPartsInBraces")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && forLoopParts(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    if (result_) {
      marker_.done(FOR_LOOP_PARTS_IN_BRACES);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'for' forLoopPartsInBraces statement
  public static boolean forStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forStatement")) return false;
    if (!nextTokenIs(builder_, FOR)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, FOR);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, forLoopPartsInBraces(builder_, level_ + 1));
    result_ = pinned_ && statement(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(FOR_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // !')'
  static boolean for_loops_parts_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_loops_parts_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !consumeToken(builder_, RPAREN);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  /* ********************************************************** */
  // '(' normalFormalParameter (',' normalFormalParameter)* (',' namedFormalParameters)? ')'
  //                       | '(' namedFormalParameters? ')'
  public static boolean formalParameterList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formalParameterList")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = formalParameterList_0(builder_, level_ + 1);
    if (!result_) result_ = formalParameterList_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(FORMAL_PARAMETER_LIST);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // '(' normalFormalParameter (',' normalFormalParameter)* (',' namedFormalParameters)? ')'
  private static boolean formalParameterList_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formalParameterList_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && normalFormalParameter(builder_, level_ + 1);
    result_ = result_ && formalParameterList_0_2(builder_, level_ + 1);
    result_ = result_ && formalParameterList_0_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (',' normalFormalParameter)*
  private static boolean formalParameterList_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formalParameterList_0_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!formalParameterList_0_2_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "formalParameterList_0_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' normalFormalParameter
  private static boolean formalParameterList_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formalParameterList_0_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && normalFormalParameter(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (',' namedFormalParameters)?
  private static boolean formalParameterList_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formalParameterList_0_3")) return false;
    formalParameterList_0_3_0(builder_, level_ + 1);
    return true;
  }

  // ',' namedFormalParameters
  private static boolean formalParameterList_0_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formalParameterList_0_3_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && namedFormalParameters(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // '(' namedFormalParameters? ')'
  private static boolean formalParameterList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formalParameterList_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && formalParameterList_1_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // namedFormalParameters?
  private static boolean formalParameterList_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formalParameterList_1_1")) return false;
    namedFormalParameters(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // arrowBodyWithSemi | block
  public static boolean functionBody(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionBody")) return false;
    if (!nextTokenIs(builder_, EXPRESSION_BODY_DEF) && !nextTokenIs(builder_, LBRACE)
        && replaceVariants(builder_, 2, "<function body>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<function body>");
    result_ = arrowBodyWithSemi(builder_, level_ + 1);
    if (!result_) result_ = block(builder_, level_ + 1);
    if (result_) {
      marker_.done(FUNCTION_BODY);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // 'native' functionBody
  //                                | functionNative
  //                                | functionBody
  static boolean functionBodyOrNative(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionBodyOrNative")) return false;
    if (!nextTokenIs(builder_, EXPRESSION_BODY_DEF) && !nextTokenIs(builder_, NATIVE)
        && !nextTokenIs(builder_, LBRACE)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = functionBodyOrNative_0(builder_, level_ + 1);
    if (!result_) result_ = functionNative(builder_, level_ + 1);
    if (!result_) result_ = functionBody(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // 'native' functionBody
  private static boolean functionBodyOrNative_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionBodyOrNative_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, NATIVE);
    result_ = result_ && functionBody(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // functionDeclarationPrivate
  public static boolean functionDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclaration")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<function declaration>");
    result_ = functionDeclarationPrivate(builder_, level_ + 1);
    if (result_) {
      marker_.done(FUNCTION_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // (returnType componentName formalParameterList | componentName formalParameterList) initializers?
  static boolean functionDeclarationPrivate(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationPrivate")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = functionDeclarationPrivate_0(builder_, level_ + 1);
    result_ = result_ && functionDeclarationPrivate_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // returnType componentName formalParameterList | componentName formalParameterList
  private static boolean functionDeclarationPrivate_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationPrivate_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = functionDeclarationPrivate_0_0(builder_, level_ + 1);
    if (!result_) result_ = functionDeclarationPrivate_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // returnType componentName formalParameterList
  private static boolean functionDeclarationPrivate_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationPrivate_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = returnType(builder_, level_ + 1);
    result_ = result_ && componentName(builder_, level_ + 1);
    result_ = result_ && formalParameterList(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // componentName formalParameterList
  private static boolean functionDeclarationPrivate_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationPrivate_0_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = componentName(builder_, level_ + 1);
    result_ = result_ && formalParameterList(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // initializers?
  private static boolean functionDeclarationPrivate_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationPrivate_1")) return false;
    initializers(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // (returnType componentName formalParameterList | componentName formalParameterList) functionBody
  public static boolean functionDeclarationWithBody(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBody")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<function declaration with body>");
    result_ = functionDeclarationWithBody_0(builder_, level_ + 1);
    result_ = result_ && functionBody(builder_, level_ + 1);
    if (result_) {
      marker_.done(FUNCTION_DECLARATION_WITH_BODY);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // returnType componentName formalParameterList | componentName formalParameterList
  private static boolean functionDeclarationWithBody_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBody_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = functionDeclarationWithBody_0_0(builder_, level_ + 1);
    if (!result_) result_ = functionDeclarationWithBody_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // returnType componentName formalParameterList
  private static boolean functionDeclarationWithBody_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBody_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = returnType(builder_, level_ + 1);
    result_ = result_ && componentName(builder_, level_ + 1);
    result_ = result_ && formalParameterList(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // componentName formalParameterList
  private static boolean functionDeclarationWithBody_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBody_0_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = componentName(builder_, level_ + 1);
    result_ = result_ && formalParameterList(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'external'? (returnType componentName formalParameterList | componentName formalParameterList) ( ';' | functionBodyOrNative)
  public static boolean functionDeclarationWithBodyOrNative(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBodyOrNative")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<function declaration with body or native>");
    result_ = functionDeclarationWithBodyOrNative_0(builder_, level_ + 1);
    result_ = result_ && functionDeclarationWithBodyOrNative_1(builder_, level_ + 1);
    result_ = result_ && functionDeclarationWithBodyOrNative_2(builder_, level_ + 1);
    if (result_) {
      marker_.done(FUNCTION_DECLARATION_WITH_BODY_OR_NATIVE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // 'external'?
  private static boolean functionDeclarationWithBodyOrNative_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBodyOrNative_0")) return false;
    consumeToken(builder_, EXTERNAL);
    return true;
  }

  // returnType componentName formalParameterList | componentName formalParameterList
  private static boolean functionDeclarationWithBodyOrNative_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBodyOrNative_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = functionDeclarationWithBodyOrNative_1_0(builder_, level_ + 1);
    if (!result_) result_ = functionDeclarationWithBodyOrNative_1_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // returnType componentName formalParameterList
  private static boolean functionDeclarationWithBodyOrNative_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBodyOrNative_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = returnType(builder_, level_ + 1);
    result_ = result_ && componentName(builder_, level_ + 1);
    result_ = result_ && formalParameterList(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // componentName formalParameterList
  private static boolean functionDeclarationWithBodyOrNative_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBodyOrNative_1_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = componentName(builder_, level_ + 1);
    result_ = result_ && formalParameterList(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ';' | functionBodyOrNative
  private static boolean functionDeclarationWithBodyOrNative_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDeclarationWithBodyOrNative_2")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = functionBodyOrNative(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // (returnType componentName | componentName)? formalParameterList functionExpressionBody
  public static boolean functionExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionExpression")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<function expression>");
    result_ = functionExpression_0(builder_, level_ + 1);
    result_ = result_ && formalParameterList(builder_, level_ + 1);
    result_ = result_ && functionExpressionBody(builder_, level_ + 1);
    if (result_) {
      marker_.done(FUNCTION_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // (returnType componentName | componentName)?
  private static boolean functionExpression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionExpression_0")) return false;
    functionExpression_0_0(builder_, level_ + 1);
    return true;
  }

  // returnType componentName | componentName
  private static boolean functionExpression_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionExpression_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = functionExpression_0_0_0(builder_, level_ + 1);
    if (!result_) result_ = componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // returnType componentName
  private static boolean functionExpression_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionExpression_0_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = returnType(builder_, level_ + 1);
    result_ = result_ && componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // arrowBody | block
  public static boolean functionExpressionBody(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionExpressionBody")) return false;
    if (!nextTokenIs(builder_, EXPRESSION_BODY_DEF) && !nextTokenIs(builder_, LBRACE)
        && replaceVariants(builder_, 2, "<function expression body>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<function expression body>");
    result_ = arrowBody(builder_, level_ + 1);
    if (!result_) result_ = block(builder_, level_ + 1);
    if (result_) {
      marker_.done(FUNCTION_EXPRESSION_BODY);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // 'native' (stringLiteralExpression ';' | ';' | stringLiteralExpression functionBody)
  static boolean functionNative(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionNative")) return false;
    if (!nextTokenIs(builder_, NATIVE)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, NATIVE);
    result_ = result_ && functionNative_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // stringLiteralExpression ';' | ';' | stringLiteralExpression functionBody
  private static boolean functionNative_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionNative_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = functionNative_1_0(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = functionNative_1_2(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // stringLiteralExpression ';'
  private static boolean functionNative_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionNative_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = stringLiteralExpression(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // stringLiteralExpression functionBody
  private static boolean functionNative_1_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionNative_1_2")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = stringLiteralExpression(builder_, level_ + 1);
    result_ = result_ && functionBody(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // returnType componentName | componentName
  static boolean functionPrefix(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionPrefix")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = functionPrefix_0(builder_, level_ + 1);
    if (!result_) result_ = componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // returnType componentName
  private static boolean functionPrefix_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionPrefix_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = returnType(builder_, level_ + 1);
    result_ = result_ && componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'typedef' functionPrefix typeParameters? formalParameterList ';'
  public static boolean functionTypeAlias(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionTypeAlias")) return false;
    if (!nextTokenIs(builder_, TYPEDEF)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, TYPEDEF);
    result_ = result_ && functionPrefix(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, functionTypeAlias_2(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, formalParameterList(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (result_ || pinned_) {
      marker_.done(FUNCTION_TYPE_ALIAS);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // typeParameters?
  private static boolean functionTypeAlias_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionTypeAlias_2")) return false;
    typeParameters(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ('external' | 'static' | 'const' | 'abstract')* returnType? 'get' componentName formalParameterList? (';' | functionBodyOrNative)
  public static boolean getterDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "getterDeclaration")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<getter declaration>");
    result_ = getterDeclaration_0(builder_, level_ + 1);
    result_ = result_ && getterDeclaration_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, GET);
    result_ = result_ && componentName(builder_, level_ + 1);
    pinned_ = result_; // pin = 4
    result_ = result_ && report_error_(builder_, getterDeclaration_4(builder_, level_ + 1));
    result_ = pinned_ && getterDeclaration_5(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(GETTER_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('external' | 'static' | 'const' | 'abstract')*
  private static boolean getterDeclaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "getterDeclaration_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!getterDeclaration_0_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "getterDeclaration_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // 'external' | 'static' | 'const' | 'abstract'
  private static boolean getterDeclaration_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "getterDeclaration_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, EXTERNAL);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) result_ = consumeToken(builder_, ABSTRACT);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // returnType?
  private static boolean getterDeclaration_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "getterDeclaration_1")) return false;
    returnType(builder_, level_ + 1);
    return true;
  }

  // formalParameterList?
  private static boolean getterDeclaration_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "getterDeclaration_4")) return false;
    formalParameterList(builder_, level_ + 1);
    return true;
  }

  // ';' | functionBodyOrNative
  private static boolean getterDeclaration_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "getterDeclaration_5")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = functionBodyOrNative(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // getterDeclaration | setterDeclaration
  static boolean getterOrSetterDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "getterOrSetterDeclaration")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = getterDeclaration(builder_, level_ + 1);
    if (!result_) result_ = setterDeclaration(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'hide' libraryReferenceList
  public static boolean hideCombinator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hideCombinator")) return false;
    if (!nextTokenIs(builder_, HIDE)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, HIDE);
    result_ = result_ && libraryReferenceList(builder_, level_ + 1);
    if (result_) {
      marker_.done(HIDE_COMBINATOR);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean id(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "id")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, IDENTIFIER);
    if (result_) {
      marker_.done(ID);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'if' '(' expressionWithRecoverUntilParen ')' statement ('else' statement)?
  public static boolean ifStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ifStatement")) return false;
    if (!nextTokenIs(builder_, IF)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, IF);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, consumeToken(builder_, LPAREN));
    result_ = pinned_ && report_error_(builder_, expressionWithRecoverUntilParen(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RPAREN)) && result_;
    result_ = pinned_ && report_error_(builder_, statement(builder_, level_ + 1)) && result_;
    result_ = pinned_ && ifStatement_5(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(IF_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('else' statement)?
  private static boolean ifStatement_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ifStatement_5")) return false;
    ifStatement_5_0(builder_, level_ + 1);
    return true;
  }

  // 'else' statement
  private static boolean ifStatement_5_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ifStatement_5_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, ELSE);
    result_ = result_ && statement(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // oldImportStatement | newImportStatement
  public static boolean importStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "importStatement")) return false;
    if (!nextTokenIs(builder_, HASH) && !nextTokenIs(builder_, IMPORT)
        && replaceVariants(builder_, 2, "<import statement>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<import statement>");
    result_ = oldImportStatement(builder_, level_ + 1);
    if (!result_) result_ = newImportStatement(builder_, level_ + 1);
    if (result_) {
      marker_.done(IMPORT_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // ':' superCallOrFieldInitializer (',' superCallOrFieldInitializer)*
  public static boolean initializers(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "initializers")) return false;
    if (!nextTokenIs(builder_, COLON)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && superCallOrFieldInitializer(builder_, level_ + 1);
    result_ = result_ && initializers_2(builder_, level_ + 1);
    if (result_) {
      marker_.done(INITIALIZERS);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // (',' superCallOrFieldInitializer)*
  private static boolean initializers_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "initializers_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!initializers_2_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "initializers_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' superCallOrFieldInitializer
  private static boolean initializers_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "initializers_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && superCallOrFieldInitializer(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // (metadata* interfaceMemberDefinition)*
  public static boolean interfaceBody(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaceBody")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<interface body>");
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!interfaceBody_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "interfaceBody");
        break;
      }
      offset_ = next_offset_;
    }
    marker_.done(INTERFACE_BODY);
    exitErrorRecordingSection(builder_, level_, true, false, _SECTION_RECOVER_, simple_scope_recover_parser_);
    return true;
  }

  // metadata* interfaceMemberDefinition
  private static boolean interfaceBody_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaceBody_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = interfaceBody_0_0(builder_, level_ + 1);
    result_ = result_ && interfaceMemberDefinition(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // metadata*
  private static boolean interfaceBody_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaceBody_0_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!metadata(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "interfaceBody_0_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // 'interface' componentName typeParameters? superinterfaces? defaultFactroy? factorySpecification? '{' interfaceBody '}'
  public static boolean interfaceDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaceDefinition")) return false;
    if (!nextTokenIs(builder_, INTERFACE)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, INTERFACE);
    result_ = result_ && componentName(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, interfaceDefinition_2(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, interfaceDefinition_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, interfaceDefinition_4(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, interfaceDefinition_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, LBRACE)) && result_;
    result_ = pinned_ && report_error_(builder_, interfaceBody(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, RBRACE) && result_;
    if (result_ || pinned_) {
      marker_.done(INTERFACE_DEFINITION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // typeParameters?
  private static boolean interfaceDefinition_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaceDefinition_2")) return false;
    typeParameters(builder_, level_ + 1);
    return true;
  }

  // superinterfaces?
  private static boolean interfaceDefinition_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaceDefinition_3")) return false;
    superinterfaces(builder_, level_ + 1);
    return true;
  }

  // defaultFactroy?
  private static boolean interfaceDefinition_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaceDefinition_4")) return false;
    defaultFactroy(builder_, level_ + 1);
    return true;
  }

  // factorySpecification?
  private static boolean interfaceDefinition_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaceDefinition_5")) return false;
    factorySpecification(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // methodPrototypeDeclaration
  //                                     | varDeclarationListWithSemicolon
  //                                     | namedConstructorDeclaration
  //                                     | getterOrSetterDeclaration
  //                                     | operatorPrototypeWithSemicolon
  static boolean interfaceMemberDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaceMemberDefinition")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, null);
    result_ = methodPrototypeDeclaration(builder_, level_ + 1);
    if (!result_) result_ = varDeclarationListWithSemicolon(builder_, level_ + 1);
    if (!result_) result_ = namedConstructorDeclaration(builder_, level_ + 1);
    if (!result_) result_ = getterOrSetterDeclaration(builder_, level_ + 1);
    if (!result_) result_ = operatorPrototypeWithSemicolon(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, interface_member_definition_recover_parser_);
    return result_;
  }

  /* ********************************************************** */
  // !('abstract' | 'assert' | 'class' | 'const' | 'extends' | 'factory' | 'final' | 'get'
  //                                                | 'implements' | 'import' | 'interface' | 'is' | 'library' | 'native' | 'negate'
  //                                                | 'operator' | 'set' | '@' | 'source' | 'static' | 'typedef' | 'var' | '}' | <<nonStrictID>>)
  static boolean interface_member_definition_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interface_member_definition_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !interface_member_definition_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // 'abstract' | 'assert' | 'class' | 'const' | 'extends' | 'factory' | 'final' | 'get'
  //                                                | 'implements' | 'import' | 'interface' | 'is' | 'library' | 'native' | 'negate'
  //                                                | 'operator' | 'set' | '@' | 'source' | 'static' | 'typedef' | 'var' | '}' | <<nonStrictID>>
  private static boolean interface_member_definition_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interface_member_definition_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, ABSTRACT);
    if (!result_) result_ = consumeToken(builder_, ASSERT);
    if (!result_) result_ = consumeToken(builder_, CLASS);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) result_ = consumeToken(builder_, EXTENDS);
    if (!result_) result_ = consumeToken(builder_, FACTORY);
    if (!result_) result_ = consumeToken(builder_, FINAL);
    if (!result_) result_ = consumeToken(builder_, GET);
    if (!result_) result_ = consumeToken(builder_, IMPLEMENTS);
    if (!result_) result_ = consumeToken(builder_, IMPORT);
    if (!result_) result_ = consumeToken(builder_, INTERFACE);
    if (!result_) result_ = consumeToken(builder_, IS);
    if (!result_) result_ = consumeToken(builder_, LIBRARY);
    if (!result_) result_ = consumeToken(builder_, NATIVE);
    if (!result_) result_ = consumeToken(builder_, NEGATE);
    if (!result_) result_ = consumeToken(builder_, OPERATOR);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, AT);
    if (!result_) result_ = consumeToken(builder_, SOURCE);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, TYPEDEF);
    if (!result_) result_ = consumeToken(builder_, VAR);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    if (!result_) result_ = nonStrictID(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'implements' typeList
  public static boolean interfaces(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interfaces")) return false;
    if (!nextTokenIs(builder_, IMPLEMENTS)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, IMPLEMENTS);
    pinned_ = result_; // pin = 1
    result_ = result_ && typeList(builder_, level_ + 1);
    if (result_ || pinned_) {
      marker_.done(INTERFACES);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'is' '!'? type
  public static boolean isExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "isExpression")) return false;
    if (!nextTokenIs(builder_, IS)) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "isExpression")) return false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, IS);
    result_ = result_ && isExpression_1(builder_, level_ + 1);
    result_ = result_ && type(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(IS_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // '!'?
  private static boolean isExpression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "isExpression_1")) return false;
    consumeToken(builder_, NOT);
    return true;
  }

  /* ********************************************************** */
  // '...' ternaryExpressionWrapper
  public static boolean iteratorExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "iteratorExpression")) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "iteratorExpression")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<iterator expression>");
    result_ = consumeToken(builder_, "...");
    result_ = result_ && ternaryExpressionWrapper(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(ITERATOR_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // ternaryExpressionWrapper iteratorExpression?
  static boolean iteratorExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "iteratorExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = ternaryExpressionWrapper(builder_, level_ + 1);
    result_ = result_ && iteratorExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // iteratorExpression?
  private static boolean iteratorExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "iteratorExpressionWrapper_1")) return false;
    iteratorExpression(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // componentName ':'
  public static boolean label(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<label>");
    result_ = componentName(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    if (result_) {
      marker_.done(LABEL);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // << nonStrictID >>
  public static boolean libraryComponentReferenceExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryComponentReferenceExpression")) return false;
    boolean result_ = false;
    int start_ = builder_.getCurrentOffset();
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<library component reference expression>");
    result_ = nonStrictID(builder_, level_ + 1);
    LighterASTNode last_ = result_? builder_.getLatestDoneMarker() : null;
    if (last_ != null && last_.getStartOffset() == start_ && type_extends_(last_.getTokenType(), LIBRARY_COMPONENT_REFERENCE_EXPRESSION)) {
      marker_.drop();
    }
    else if (result_) {
      marker_.done(LIBRARY_COMPONENT_REFERENCE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // <<nonStrictID>> ('.' <<nonStrictID>>)*
  public static boolean libraryId(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryId")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<library id>");
    result_ = nonStrictID(builder_, level_ + 1);
    result_ = result_ && libraryId_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(LIBRARY_ID);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, semicolon_recover_parser_);
    return result_;
  }

  // ('.' <<nonStrictID>>)*
  private static boolean libraryId_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryId_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!libraryId_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "libraryId_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // '.' <<nonStrictID>>
  private static boolean libraryId_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryId_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && nonStrictID(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // libraryComponentReferenceExpression (',' libraryComponentReferenceExpression)*
  public static boolean libraryReferenceList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryReferenceList")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<library reference list>");
    result_ = libraryComponentReferenceExpression(builder_, level_ + 1);
    result_ = result_ && libraryReferenceList_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(LIBRARY_REFERENCE_LIST);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // (',' libraryComponentReferenceExpression)*
  private static boolean libraryReferenceList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryReferenceList_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!libraryReferenceList_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "libraryReferenceList_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' libraryComponentReferenceExpression
  private static boolean libraryReferenceList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryReferenceList_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && libraryComponentReferenceExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '#' 'library' '(' pathOrLibraryReference ')' ';' |
  //                      'library' qualifiedComponentName ';'
  public static boolean libraryStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryStatement")) return false;
    if (!nextTokenIs(builder_, HASH) && !nextTokenIs(builder_, LIBRARY)
        && replaceVariants(builder_, 2, "<library statement>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<library statement>");
    result_ = libraryStatement_0(builder_, level_ + 1);
    if (!result_) result_ = libraryStatement_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(LIBRARY_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // '#' 'library' '(' pathOrLibraryReference ')' ';'
  private static boolean libraryStatement_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryStatement_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, HASH);
    result_ = result_ && consumeToken(builder_, LIBRARY);
    result_ = result_ && consumeToken(builder_, LPAREN);
    result_ = result_ && pathOrLibraryReference(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // 'library' qualifiedComponentName ';'
  private static boolean libraryStatement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "libraryStatement_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LIBRARY);
    result_ = result_ && qualifiedComponentName(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '[' (expressionList ','?)? ']'
  public static boolean listLiteralExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "listLiteralExpression")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && listLiteralExpression_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    if (result_) {
      marker_.done(LIST_LITERAL_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // (expressionList ','?)?
  private static boolean listLiteralExpression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "listLiteralExpression_1")) return false;
    listLiteralExpression_1_0(builder_, level_ + 1);
    return true;
  }

  // expressionList ','?
  private static boolean listLiteralExpression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "listLiteralExpression_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = expressionList(builder_, level_ + 1);
    result_ = result_ && listLiteralExpression_1_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ','?
  private static boolean listLiteralExpression_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "listLiteralExpression_1_0_1")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // 'null' | 'true' | 'false' | HEX_NUMBER | NUMBER | stringLiteralExpression | listLiteralExpression | mapLiteralExpression
  public static boolean literalExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "literalExpression")) return false;
    if (!nextTokenIs(builder_, LBRACKET) && !nextTokenIs(builder_, FALSE)
        && !nextTokenIs(builder_, NULL) && !nextTokenIs(builder_, TRUE)
        && !nextTokenIs(builder_, LBRACE) && !nextTokenIs(builder_, HEX_NUMBER)
        && !nextTokenIs(builder_, NUMBER) && !nextTokenIs(builder_, OPEN_QUOTE)
        && !nextTokenIs(builder_, RAW_SINGLE_QUOTED_STRING) && !nextTokenIs(builder_, RAW_TRIPLE_QUOTED_STRING)
        && replaceVariants(builder_, 10, "<literal expression>")) return false;
    boolean result_ = false;
    int start_ = builder_.getCurrentOffset();
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<literal expression>");
    result_ = consumeToken(builder_, NULL);
    if (!result_) result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    if (!result_) result_ = consumeToken(builder_, HEX_NUMBER);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = stringLiteralExpression(builder_, level_ + 1);
    if (!result_) result_ = listLiteralExpression(builder_, level_ + 1);
    if (!result_) result_ = mapLiteralExpression(builder_, level_ + 1);
    LighterASTNode last_ = result_? builder_.getLatestDoneMarker() : null;
    if (last_ != null && last_.getStartOffset() == start_ && type_extends_(last_.getTokenType(), LITERAL_EXPRESSION)) {
      marker_.drop();
    }
    else if (result_) {
      marker_.done(LITERAL_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // '&&' compareExpressionWrapper
  public static boolean logicAndExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logicAndExpression")) return false;
    if (!nextTokenIs(builder_, AND_AND)) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "logicAndExpression")) return false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, AND_AND);
    result_ = result_ && compareExpressionWrapper(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(LOGIC_AND_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // compareExpressionWrapper logicAndExpression*
  static boolean logicAndExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logicAndExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = compareExpressionWrapper(builder_, level_ + 1);
    result_ = result_ && logicAndExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // logicAndExpression*
  private static boolean logicAndExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logicAndExpressionWrapper_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!logicAndExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "logicAndExpressionWrapper_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // '||' logicAndExpressionWrapper
  public static boolean logicOrExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logicOrExpression")) return false;
    if (!nextTokenIs(builder_, OR_OR)) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "logicOrExpression")) return false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, OR_OR);
    result_ = result_ && logicAndExpressionWrapper(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(LOGIC_OR_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // logicAndExpressionWrapper logicOrExpression*
  static boolean logicOrExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logicOrExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = logicAndExpressionWrapper(builder_, level_ + 1);
    result_ = result_ && logicOrExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // logicOrExpression*
  private static boolean logicOrExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logicOrExpressionWrapper_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!logicOrExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "logicOrExpressionWrapper_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // LONG_TEMPLATE_ENTRY_START expression LONG_TEMPLATE_ENTRY_END
  public static boolean longTemplateEntry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "longTemplateEntry")) return false;
    if (!nextTokenIs(builder_, LONG_TEMPLATE_ENTRY_START)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, LONG_TEMPLATE_ENTRY_START);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, expression(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, LONG_TEMPLATE_ENTRY_END) && result_;
    if (result_ || pinned_) {
      marker_.done(LONG_TEMPLATE_ENTRY);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // stringLiteralExpression ':' expression
  public static boolean mapLiteralEntry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mapLiteralEntry")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<map literal entry>");
    result_ = stringLiteralExpression(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && expression(builder_, level_ + 1);
    if (result_) {
      marker_.done(MAP_LITERAL_ENTRY);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, map_literal_entry_recover_parser_);
    return result_;
  }

  /* ********************************************************** */
  // '{' (mapLiteralEntry (',' mapLiteralEntry)* ','? )? '}'
  public static boolean mapLiteralExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mapLiteralExpression")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && mapLiteralExpression_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    if (result_) {
      marker_.done(MAP_LITERAL_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // (mapLiteralEntry (',' mapLiteralEntry)* ','? )?
  private static boolean mapLiteralExpression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mapLiteralExpression_1")) return false;
    mapLiteralExpression_1_0(builder_, level_ + 1);
    return true;
  }

  // mapLiteralEntry (',' mapLiteralEntry)* ','?
  private static boolean mapLiteralExpression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mapLiteralExpression_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = mapLiteralEntry(builder_, level_ + 1);
    result_ = result_ && mapLiteralExpression_1_0_1(builder_, level_ + 1);
    result_ = result_ && mapLiteralExpression_1_0_2(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (',' mapLiteralEntry)*
  private static boolean mapLiteralExpression_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mapLiteralExpression_1_0_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!mapLiteralExpression_1_0_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "mapLiteralExpression_1_0_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' mapLiteralEntry
  private static boolean mapLiteralExpression_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mapLiteralExpression_1_0_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && mapLiteralEntry(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ','?
  private static boolean mapLiteralExpression_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mapLiteralExpression_1_0_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // !('!=' | '!==' | 'is' | '%' | '&&' | '&' | '(' | ')' | '*' | '+' | ',' | '-' | '.' | '/' | ':' | ';' | '<'
  //                                       | '<<' | '<=' | '==' | '===' | '=>' | '>' | '>=' | '?' | '[' | ']' | '^'
  //                                       | 'native' | '{' | '|' | '||' | '}' | '~/' | shiftRightOperator | ('.' '.'))
  static boolean map_literal_entry_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_literal_entry_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !map_literal_entry_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // '!=' | '!==' | 'is' | '%' | '&&' | '&' | '(' | ')' | '*' | '+' | ',' | '-' | '.' | '/' | ':' | ';' | '<'
  //                                       | '<<' | '<=' | '==' | '===' | '=>' | '>' | '>=' | '?' | '[' | ']' | '^'
  //                                       | 'native' | '{' | '|' | '||' | '}' | '~/' | shiftRightOperator | ('.' '.')
  private static boolean map_literal_entry_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_literal_entry_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, NEQ);
    if (!result_) result_ = consumeToken(builder_, NEQ_EQ);
    if (!result_) result_ = consumeToken(builder_, IS);
    if (!result_) result_ = consumeToken(builder_, REM);
    if (!result_) result_ = consumeToken(builder_, AND_AND);
    if (!result_) result_ = consumeToken(builder_, AND);
    if (!result_) result_ = consumeToken(builder_, LPAREN);
    if (!result_) result_ = consumeToken(builder_, RPAREN);
    if (!result_) result_ = consumeToken(builder_, MUL);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, DIV);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = consumeToken(builder_, LT);
    if (!result_) result_ = consumeToken(builder_, LT_LT);
    if (!result_) result_ = consumeToken(builder_, LT_EQ);
    if (!result_) result_ = consumeToken(builder_, EQ_EQ);
    if (!result_) result_ = consumeToken(builder_, EQ_EQ_EQ);
    if (!result_) result_ = consumeToken(builder_, EXPRESSION_BODY_DEF);
    if (!result_) result_ = consumeToken(builder_, GT);
    if (!result_) result_ = consumeToken(builder_, GT_EQ);
    if (!result_) result_ = consumeToken(builder_, QUEST);
    if (!result_) result_ = consumeToken(builder_, LBRACKET);
    if (!result_) result_ = consumeToken(builder_, RBRACKET);
    if (!result_) result_ = consumeToken(builder_, XOR);
    if (!result_) result_ = consumeToken(builder_, NATIVE);
    if (!result_) result_ = consumeToken(builder_, LBRACE);
    if (!result_) result_ = consumeToken(builder_, OR);
    if (!result_) result_ = consumeToken(builder_, OR_OR);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    if (!result_) result_ = consumeToken(builder_, INT_DIV);
    if (!result_) result_ = shiftRightOperator(builder_, level_ + 1);
    if (!result_) result_ = map_literal_entry_recover_0_35(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // '.' '.'
  private static boolean map_literal_entry_recover_0_35(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_literal_entry_recover_0_35")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && consumeToken(builder_, DOT);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '@' simpleQualifiedReferenceExpression arguments?
  public static boolean metadata(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "metadata")) return false;
    if (!nextTokenIs(builder_, AT)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, AT);
    result_ = result_ && simpleQualifiedReferenceExpression(builder_, level_ + 1);
    result_ = result_ && metadata_2(builder_, level_ + 1);
    if (result_) {
      marker_.done(METADATA);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // arguments?
  private static boolean metadata_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "metadata_2")) return false;
    arguments(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ('external' | 'static' | 'const' | 'abstract')* functionDeclarationPrivate initializers? (';' | functionBodyOrNative | redirection)?
  public static boolean methodDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "methodDeclaration")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<method declaration>");
    result_ = methodDeclaration_0(builder_, level_ + 1);
    result_ = result_ && functionDeclarationPrivate(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, methodDeclaration_2(builder_, level_ + 1));
    result_ = pinned_ && methodDeclaration_3(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(METHOD_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('external' | 'static' | 'const' | 'abstract')*
  private static boolean methodDeclaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "methodDeclaration_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!methodDeclaration_0_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "methodDeclaration_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // 'external' | 'static' | 'const' | 'abstract'
  private static boolean methodDeclaration_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "methodDeclaration_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, EXTERNAL);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) result_ = consumeToken(builder_, ABSTRACT);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // initializers?
  private static boolean methodDeclaration_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "methodDeclaration_2")) return false;
    initializers(builder_, level_ + 1);
    return true;
  }

  // (';' | functionBodyOrNative | redirection)?
  private static boolean methodDeclaration_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "methodDeclaration_3")) return false;
    methodDeclaration_3_0(builder_, level_ + 1);
    return true;
  }

  // ';' | functionBodyOrNative | redirection
  private static boolean methodDeclaration_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "methodDeclaration_3_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = functionBodyOrNative(builder_, level_ + 1);
    if (!result_) result_ = redirection(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'const'? functionDeclarationPrivate ';'
  public static boolean methodPrototypeDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "methodPrototypeDeclaration")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<method prototype declaration>");
    result_ = methodPrototypeDeclaration_0(builder_, level_ + 1);
    result_ = result_ && functionDeclarationPrivate(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (result_ || pinned_) {
      marker_.done(METHOD_PROTOTYPE_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // 'const'?
  private static boolean methodPrototypeDeclaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "methodPrototypeDeclaration_0")) return false;
    consumeToken(builder_, CONST);
    return true;
  }

  /* ********************************************************** */
  // 'with' typeList
  public static boolean mixins(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mixins")) return false;
    if (!nextTokenIs(builder_, WITH)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, WITH);
    pinned_ = result_; // pin = 1
    result_ = result_ && typeList(builder_, level_ + 1);
    if (result_ || pinned_) {
      marker_.done(MIXINS);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // multiplicativeOperator prefixExpression
  public static boolean multiplicativeExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicativeExpression")) return false;
    if (!nextTokenIs(builder_, REM) && !nextTokenIs(builder_, MUL)
        && !nextTokenIs(builder_, DIV) && !nextTokenIs(builder_, INT_DIV)
        && replaceVariants(builder_, 4, "<multiplicative expression>")) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "multiplicativeExpression")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<multiplicative expression>");
    result_ = multiplicativeOperator(builder_, level_ + 1);
    result_ = result_ && prefixExpression(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(MULTIPLICATIVE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // prefixExpression multiplicativeExpression*
  static boolean multiplicativeExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicativeExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = prefixExpression(builder_, level_ + 1);
    result_ = result_ && multiplicativeExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // multiplicativeExpression*
  private static boolean multiplicativeExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicativeExpressionWrapper_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!multiplicativeExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "multiplicativeExpressionWrapper_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // '*' | '/' | '%' | '~/'
  public static boolean multiplicativeOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicativeOperator")) return false;
    if (!nextTokenIs(builder_, REM) && !nextTokenIs(builder_, MUL)
        && !nextTokenIs(builder_, DIV) && !nextTokenIs(builder_, INT_DIV)
        && replaceVariants(builder_, 4, "<multiplicative operator>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<multiplicative operator>");
    result_ = consumeToken(builder_, MUL);
    if (!result_) result_ = consumeToken(builder_, DIV);
    if (!result_) result_ = consumeToken(builder_, REM);
    if (!result_) result_ = consumeToken(builder_, INT_DIV);
    if (result_) {
      marker_.done(MULTIPLICATIVE_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // label expression
  public static boolean namedArgument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedArgument")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<named argument>");
    result_ = label(builder_, level_ + 1);
    result_ = result_ && expression(builder_, level_ + 1);
    if (result_) {
      marker_.done(NAMED_ARGUMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // ('external' | 'const')* referenceExpression '.' componentName formalParameterList initializers? (';' | functionBodyOrNative | redirection)?
  public static boolean namedConstructorDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedConstructorDeclaration")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<named constructor declaration>");
    result_ = namedConstructorDeclaration_0(builder_, level_ + 1);
    result_ = result_ && referenceExpression(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, DOT);
    result_ = result_ && componentName(builder_, level_ + 1);
    result_ = result_ && formalParameterList(builder_, level_ + 1);
    pinned_ = result_; // pin = 5
    result_ = result_ && report_error_(builder_, namedConstructorDeclaration_5(builder_, level_ + 1));
    result_ = pinned_ && namedConstructorDeclaration_6(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(NAMED_CONSTRUCTOR_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('external' | 'const')*
  private static boolean namedConstructorDeclaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedConstructorDeclaration_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!namedConstructorDeclaration_0_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "namedConstructorDeclaration_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // 'external' | 'const'
  private static boolean namedConstructorDeclaration_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedConstructorDeclaration_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, EXTERNAL);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // initializers?
  private static boolean namedConstructorDeclaration_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedConstructorDeclaration_5")) return false;
    initializers(builder_, level_ + 1);
    return true;
  }

  // (';' | functionBodyOrNative | redirection)?
  private static boolean namedConstructorDeclaration_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedConstructorDeclaration_6")) return false;
    namedConstructorDeclaration_6_0(builder_, level_ + 1);
    return true;
  }

  // ';' | functionBodyOrNative | redirection
  private static boolean namedConstructorDeclaration_6_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedConstructorDeclaration_6_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = functionBodyOrNative(builder_, level_ + 1);
    if (!result_) result_ = redirection(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '[' defaultFormalNamedParameter (',' defaultFormalNamedParameter)* ']' |
  //                           '{' defaultFormalNamedParameter (',' defaultFormalNamedParameter)* '}'
  public static boolean namedFormalParameters(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedFormalParameters")) return false;
    if (!nextTokenIs(builder_, LBRACKET) && !nextTokenIs(builder_, LBRACE)
        && replaceVariants(builder_, 2, "<named formal parameters>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<named formal parameters>");
    result_ = namedFormalParameters_0(builder_, level_ + 1);
    if (!result_) result_ = namedFormalParameters_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(NAMED_FORMAL_PARAMETERS);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // '[' defaultFormalNamedParameter (',' defaultFormalNamedParameter)* ']'
  private static boolean namedFormalParameters_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedFormalParameters_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && defaultFormalNamedParameter(builder_, level_ + 1);
    result_ = result_ && namedFormalParameters_0_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (',' defaultFormalNamedParameter)*
  private static boolean namedFormalParameters_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedFormalParameters_0_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!namedFormalParameters_0_2_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "namedFormalParameters_0_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' defaultFormalNamedParameter
  private static boolean namedFormalParameters_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedFormalParameters_0_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && defaultFormalNamedParameter(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // '{' defaultFormalNamedParameter (',' defaultFormalNamedParameter)* '}'
  private static boolean namedFormalParameters_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedFormalParameters_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && defaultFormalNamedParameter(builder_, level_ + 1);
    result_ = result_ && namedFormalParameters_1_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (',' defaultFormalNamedParameter)*
  private static boolean namedFormalParameters_1_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedFormalParameters_1_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!namedFormalParameters_1_2_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "namedFormalParameters_1_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' defaultFormalNamedParameter
  private static boolean namedFormalParameters_1_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namedFormalParameters_1_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && defaultFormalNamedParameter(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '#' 'native' '(' pathOrLibraryReference ')' ';'
  public static boolean nativeStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nativeStatement")) return false;
    if (!nextTokenIs(builder_, HASH)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, HASH);
    result_ = result_ && consumeToken(builder_, NATIVE);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, consumeToken(builder_, LPAREN));
    result_ = pinned_ && report_error_(builder_, pathOrLibraryReference(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RPAREN)) && result_;
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (result_ || pinned_) {
      marker_.done(NATIVE_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'new' type ('.' referenceExpression)? arguments
  public static boolean newExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newExpression")) return false;
    if (!nextTokenIs(builder_, NEW)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, NEW);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, type(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, newExpression_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && arguments(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(NEW_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('.' referenceExpression)?
  private static boolean newExpression_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newExpression_2")) return false;
    newExpression_2_0(builder_, level_ + 1);
    return true;
  }

  // '.' referenceExpression
  private static boolean newExpression_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newExpression_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && referenceExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // (newExpression | constConstructorExpression) qualifiedReferenceTail?
  static boolean newExpressionOrConstOrCall(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newExpressionOrConstOrCall")) return false;
    if (!nextTokenIs(builder_, CONST) && !nextTokenIs(builder_, NEW)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = newExpressionOrConstOrCall_0(builder_, level_ + 1);
    result_ = result_ && newExpressionOrConstOrCall_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // newExpression | constConstructorExpression
  private static boolean newExpressionOrConstOrCall_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newExpressionOrConstOrCall_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = newExpression(builder_, level_ + 1);
    if (!result_) result_ = constConstructorExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // qualifiedReferenceTail?
  private static boolean newExpressionOrConstOrCall_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newExpressionOrConstOrCall_1")) return false;
    qualifiedReferenceTail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'import' pathOrLibraryReference ('as' componentName )? combinator* ';'
  static boolean newImportStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newImportStatement")) return false;
    if (!nextTokenIs(builder_, IMPORT)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, IMPORT);
    result_ = result_ && pathOrLibraryReference(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, newImportStatement_2(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, newImportStatement_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('as' componentName )?
  private static boolean newImportStatement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newImportStatement_2")) return false;
    newImportStatement_2_0(builder_, level_ + 1);
    return true;
  }

  // 'as' componentName
  private static boolean newImportStatement_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newImportStatement_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, AS);
    result_ = result_ && componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // combinator*
  private static boolean newImportStatement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "newImportStatement_3")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!combinator(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "newImportStatement_3");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // block // Guard to break tie with map literal.
  //                                | metadata* functionDeclarationWithBody ';'?
  //                                | whileStatement ';'?
  //                                | doWhileStatement ';'?
  //                                | forStatement ';'?
  //                                | ifStatement ';'?
  //                                | switchStatement ';'?
  //                                | tryStatement
  //                                | breakStatement
  //                                | continueStatement
  //                                | returnStatement
  //                                | throwStatement
  //                                | assertStatement
  //                                | statementFollowedBySemiColon
  //                                | ';'
  static boolean nonLabelledStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = block(builder_, level_ + 1);
    if (!result_) result_ = nonLabelledStatement_1(builder_, level_ + 1);
    if (!result_) result_ = nonLabelledStatement_2(builder_, level_ + 1);
    if (!result_) result_ = nonLabelledStatement_3(builder_, level_ + 1);
    if (!result_) result_ = nonLabelledStatement_4(builder_, level_ + 1);
    if (!result_) result_ = nonLabelledStatement_5(builder_, level_ + 1);
    if (!result_) result_ = nonLabelledStatement_6(builder_, level_ + 1);
    if (!result_) result_ = tryStatement(builder_, level_ + 1);
    if (!result_) result_ = breakStatement(builder_, level_ + 1);
    if (!result_) result_ = continueStatement(builder_, level_ + 1);
    if (!result_) result_ = returnStatement(builder_, level_ + 1);
    if (!result_) result_ = throwStatement(builder_, level_ + 1);
    if (!result_) result_ = assertStatement(builder_, level_ + 1);
    if (!result_) result_ = statementFollowedBySemiColon(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // metadata* functionDeclarationWithBody ';'?
  private static boolean nonLabelledStatement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = nonLabelledStatement_1_0(builder_, level_ + 1);
    result_ = result_ && functionDeclarationWithBody(builder_, level_ + 1);
    result_ = result_ && nonLabelledStatement_1_2(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // metadata*
  private static boolean nonLabelledStatement_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_1_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!metadata(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "nonLabelledStatement_1_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ';'?
  private static boolean nonLabelledStatement_1_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_1_2")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  // whileStatement ';'?
  private static boolean nonLabelledStatement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_2")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = whileStatement(builder_, level_ + 1);
    result_ = result_ && nonLabelledStatement_2_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ';'?
  private static boolean nonLabelledStatement_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_2_1")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  // doWhileStatement ';'?
  private static boolean nonLabelledStatement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_3")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = doWhileStatement(builder_, level_ + 1);
    result_ = result_ && nonLabelledStatement_3_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ';'?
  private static boolean nonLabelledStatement_3_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_3_1")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  // forStatement ';'?
  private static boolean nonLabelledStatement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_4")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = forStatement(builder_, level_ + 1);
    result_ = result_ && nonLabelledStatement_4_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ';'?
  private static boolean nonLabelledStatement_4_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_4_1")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  // ifStatement ';'?
  private static boolean nonLabelledStatement_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_5")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = ifStatement(builder_, level_ + 1);
    result_ = result_ && nonLabelledStatement_5_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ';'?
  private static boolean nonLabelledStatement_5_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_5_1")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  // switchStatement ';'?
  private static boolean nonLabelledStatement_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_6")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = switchStatement(builder_, level_ + 1);
    result_ = result_ && nonLabelledStatement_6_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ';'?
  private static boolean nonLabelledStatement_6_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonLabelledStatement_6_1")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // functionDeclaration
  //                         | fieldFormalParameter
  //                         | varDeclaration
  //                         | componentName
  public static boolean normalFormalParameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "normalFormalParameter")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<normal formal parameter>");
    result_ = functionDeclaration(builder_, level_ + 1);
    if (!result_) result_ = fieldFormalParameter(builder_, level_ + 1);
    if (!result_) result_ = varDeclaration(builder_, level_ + 1);
    if (!result_) result_ = componentName(builder_, level_ + 1);
    if (result_) {
      marker_.done(NORMAL_FORMAL_PARAMETER);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // !')'
  static boolean not_paren_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_paren_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !consumeToken(builder_, RPAREN);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  /* ********************************************************** */
  // '#' 'import' '(' pathOrLibraryReference (',' IDENTIFIER ':' stringLiteralExpression)? ')' ';'
  static boolean oldImportStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "oldImportStatement")) return false;
    if (!nextTokenIs(builder_, HASH)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, HASH);
    result_ = result_ && consumeToken(builder_, IMPORT);
    result_ = result_ && consumeToken(builder_, LPAREN);
    result_ = result_ && pathOrLibraryReference(builder_, level_ + 1);
    pinned_ = result_; // pin = 4
    result_ = result_ && report_error_(builder_, oldImportStatement_4(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RPAREN)) && result_;
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // (',' IDENTIFIER ':' stringLiteralExpression)?
  private static boolean oldImportStatement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "oldImportStatement_4")) return false;
    oldImportStatement_4_0(builder_, level_ + 1);
    return true;
  }

  // ',' IDENTIFIER ':' stringLiteralExpression
  private static boolean oldImportStatement_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "oldImportStatement_4_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && consumeToken(builder_, IDENTIFIER);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && stringLiteralExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'on' type catchPart?
  public static boolean onPart(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "onPart")) return false;
    if (!nextTokenIs(builder_, ON)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, ON);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, type(builder_, level_ + 1));
    result_ = pinned_ && onPart_2(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(ON_PART);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // catchPart?
  private static boolean onPart_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "onPart_2")) return false;
    catchPart(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // returnType? 'operator' userDefinableOperator formalParameterList (';' | functionBodyOrNative)
  public static boolean operatorDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "operatorDeclaration")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<operator declaration>");
    result_ = operatorDeclaration_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, OPERATOR);
    result_ = result_ && userDefinableOperator(builder_, level_ + 1);
    pinned_ = result_; // pin = 3
    result_ = result_ && report_error_(builder_, formalParameterList(builder_, level_ + 1));
    result_ = pinned_ && operatorDeclaration_4(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(OPERATOR_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // returnType?
  private static boolean operatorDeclaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "operatorDeclaration_0")) return false;
    returnType(builder_, level_ + 1);
    return true;
  }

  // ';' | functionBodyOrNative
  private static boolean operatorDeclaration_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "operatorDeclaration_4")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = functionBodyOrNative(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // returnType? 'operator' userDefinableOperator formalParameterList
  public static boolean operatorPrototype(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "operatorPrototype")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<operator prototype>");
    result_ = operatorPrototype_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, OPERATOR);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, userDefinableOperator(builder_, level_ + 1));
    result_ = pinned_ && formalParameterList(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(OPERATOR_PROTOTYPE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // returnType?
  private static boolean operatorPrototype_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "operatorPrototype_0")) return false;
    returnType(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // operatorPrototype ';'
  static boolean operatorPrototypeWithSemicolon(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "operatorPrototypeWithSemicolon")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = operatorPrototype(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '(' (expression | statement) ')'
  public static boolean parenthesizedExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesizedExpression")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && parenthesizedExpression_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    if (result_) {
      marker_.done(PARENTHESIZED_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // expression | statement
  private static boolean parenthesizedExpression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesizedExpression_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = expression(builder_, level_ + 1);
    if (!result_) result_ = statement(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'part' 'of' libraryId ';'
  public static boolean partOfStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "partOfStatement")) return false;
    if (!nextTokenIs(builder_, PART)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, PART);
    result_ = result_ && consumeToken(builder_, OF);
    result_ = result_ && libraryId(builder_, level_ + 1);
    pinned_ = result_; // pin = 3
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (result_ || pinned_) {
      marker_.done(PART_OF_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // stringLiteralExpression
  public static boolean pathOrLibraryReference(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pathOrLibraryReference")) return false;
    if (!nextTokenIs(builder_, OPEN_QUOTE) && !nextTokenIs(builder_, RAW_SINGLE_QUOTED_STRING)
        && !nextTokenIs(builder_, RAW_TRIPLE_QUOTED_STRING) && replaceVariants(builder_, 3, "<path or library reference>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<path or library reference>");
    result_ = stringLiteralExpression(builder_, level_ + 1);
    if (result_) {
      marker_.done(PATH_OR_LIBRARY_REFERENCE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // prefixOperator prefixExpression | suffixExpressionWrapper
  public static boolean prefixExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prefixExpression")) return false;
    boolean result_ = false;
    int start_ = builder_.getCurrentOffset();
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<prefix expression>");
    result_ = prefixExpression_0(builder_, level_ + 1);
    if (!result_) result_ = suffixExpressionWrapper(builder_, level_ + 1);
    LighterASTNode last_ = result_? builder_.getLatestDoneMarker() : null;
    if (last_ != null && last_.getStartOffset() == start_ && type_extends_(last_.getTokenType(), PREFIX_EXPRESSION)) {
      marker_.drop();
    }
    else if (result_) {
      marker_.done(PREFIX_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // prefixOperator prefixExpression
  private static boolean prefixExpression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prefixExpression_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = prefixOperator(builder_, level_ + 1);
    result_ = result_ && prefixExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '-' | '+' | '--' | '++' | '!' | '~' | '?'
  public static boolean prefixOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prefixOperator")) return false;
    if (!nextTokenIs(builder_, NOT) && !nextTokenIs(builder_, PLUS)
        && !nextTokenIs(builder_, PLUS_PLUS) && !nextTokenIs(builder_, MINUS)
        && !nextTokenIs(builder_, MINUS_MINUS) && !nextTokenIs(builder_, QUEST)
        && !nextTokenIs(builder_, BIN_NOT) && replaceVariants(builder_, 7, "<prefix operator>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<prefix operator>");
    result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS_MINUS);
    if (!result_) result_ = consumeToken(builder_, PLUS_PLUS);
    if (!result_) result_ = consumeToken(builder_, NOT);
    if (!result_) result_ = consumeToken(builder_, BIN_NOT);
    if (!result_) result_ = consumeToken(builder_, QUEST);
    if (result_) {
      marker_.done(PREFIX_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // << nonStrictID >> ('.' << nonStrictID >>)*
  public static boolean qualifiedComponentName(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedComponentName")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<qualified component name>");
    result_ = nonStrictID(builder_, level_ + 1);
    result_ = result_ && qualifiedComponentName_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(QUALIFIED_COMPONENT_NAME);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // ('.' << nonStrictID >>)*
  private static boolean qualifiedComponentName_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedComponentName_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!qualifiedComponentName_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "qualifiedComponentName_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // '.' << nonStrictID >>
  private static boolean qualifiedComponentName_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedComponentName_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && nonStrictID(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '.' referenceExpression
  public static boolean qualifiedReferenceExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedReferenceExpression")) return false;
    if (!nextTokenIs(builder_, DOT)) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "qualifiedReferenceExpression")) return false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && referenceExpression(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(REFERENCE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // qualifiedReferenceExpression (callExpression | arrayAccessExpression | qualifiedReferenceExpression)*
  static boolean qualifiedReferenceTail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedReferenceTail")) return false;
    if (!nextTokenIs(builder_, DOT)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = qualifiedReferenceExpression(builder_, level_ + 1);
    result_ = result_ && qualifiedReferenceTail_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (callExpression | arrayAccessExpression | qualifiedReferenceExpression)*
  private static boolean qualifiedReferenceTail_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedReferenceTail_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!qualifiedReferenceTail_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "qualifiedReferenceTail_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // callExpression | arrayAccessExpression | qualifiedReferenceExpression
  private static boolean qualifiedReferenceTail_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedReferenceTail_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = callExpression(builder_, level_ + 1);
    if (!result_) result_ = arrayAccessExpression(builder_, level_ + 1);
    if (!result_) result_ = qualifiedReferenceExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // ':' 'this' ('.' referenceExpression)? arguments
  public static boolean redirection(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "redirection")) return false;
    if (!nextTokenIs(builder_, COLON)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && consumeToken(builder_, THIS);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, redirection_2(builder_, level_ + 1));
    result_ = pinned_ && arguments(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(REDIRECTION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('.' referenceExpression)?
  private static boolean redirection_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "redirection_2")) return false;
    redirection_2_0(builder_, level_ + 1);
    return true;
  }

  // '.' referenceExpression
  private static boolean redirection_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "redirection_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && referenceExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // << nonStrictID >>
  public static boolean referenceExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "referenceExpression")) return false;
    boolean result_ = false;
    int start_ = builder_.getCurrentOffset();
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<reference expression>");
    result_ = nonStrictID(builder_, level_ + 1);
    LighterASTNode last_ = result_? builder_.getLatestDoneMarker() : null;
    if (last_ != null && last_.getStartOffset() == start_ && type_extends_(last_.getTokenType(), REFERENCE_EXPRESSION)) {
      marker_.drop();
    }
    else if (result_) {
      marker_.done(REFERENCE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // '>=' | '>' | '<=' | '<'
  public static boolean relationalOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "relationalOperator")) return false;
    if (!nextTokenIs(builder_, LT) && !nextTokenIs(builder_, LT_EQ)
        && !nextTokenIs(builder_, GT) && !nextTokenIs(builder_, GT_EQ)
        && replaceVariants(builder_, 4, "<relational operator>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<relational operator>");
    result_ = consumeToken(builder_, GT_EQ);
    if (!result_) result_ = consumeToken(builder_, GT);
    if (!result_) result_ = consumeToken(builder_, LT_EQ);
    if (!result_) result_ = consumeToken(builder_, LT);
    if (result_) {
      marker_.done(RELATIONAL_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // '#' 'resource' '(' pathOrLibraryReference ')' ';'
  public static boolean resourceStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resourceStatement")) return false;
    if (!nextTokenIs(builder_, HASH)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, HASH);
    result_ = result_ && consumeToken(builder_, RESOURCE);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, consumeToken(builder_, LPAREN));
    result_ = pinned_ && report_error_(builder_, pathOrLibraryReference(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RPAREN)) && result_;
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (result_ || pinned_) {
      marker_.done(RESOURCE_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'return' expression? ';'
  public static boolean returnStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "returnStatement")) return false;
    if (!nextTokenIs(builder_, RETURN)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, RETURN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, returnStatement_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (result_ || pinned_) {
      marker_.done(RETURN_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // expression?
  private static boolean returnStatement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "returnStatement_1")) return false;
    expression(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // type | 'var'
  public static boolean returnType(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "returnType")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<return type>");
    result_ = type(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, VAR);
    if (result_) {
      marker_.done(RETURN_TYPE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // !(';')
  static boolean semicolon_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "semicolon_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !semicolon_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // (';')
  private static boolean semicolon_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "semicolon_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // ('external' | 'static' | 'const' | 'abstract')* returnType? 'set' componentName formalParameterList (';' | functionBodyOrNative)
  public static boolean setterDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "setterDeclaration")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<setter declaration>");
    result_ = setterDeclaration_0(builder_, level_ + 1);
    result_ = result_ && setterDeclaration_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SET);
    result_ = result_ && componentName(builder_, level_ + 1);
    pinned_ = result_; // pin = 4
    result_ = result_ && report_error_(builder_, formalParameterList(builder_, level_ + 1));
    result_ = pinned_ && setterDeclaration_5(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(SETTER_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // ('external' | 'static' | 'const' | 'abstract')*
  private static boolean setterDeclaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "setterDeclaration_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!setterDeclaration_0_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "setterDeclaration_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // 'external' | 'static' | 'const' | 'abstract'
  private static boolean setterDeclaration_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "setterDeclaration_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, EXTERNAL);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) result_ = consumeToken(builder_, ABSTRACT);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // returnType?
  private static boolean setterDeclaration_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "setterDeclaration_1")) return false;
    returnType(builder_, level_ + 1);
    return true;
  }

  // ';' | functionBodyOrNative
  private static boolean setterDeclaration_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "setterDeclaration_5")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = functionBodyOrNative(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // shiftOperator additiveExpressionWrapper
  public static boolean shiftExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shiftExpression")) return false;
    if (!nextTokenIs(builder_, LT_LT) && !nextTokenIs(builder_, GT)
        && replaceVariants(builder_, 2, "<shift expression>")) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "shiftExpression")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<shift expression>");
    result_ = shiftOperator(builder_, level_ + 1);
    result_ = result_ && additiveExpressionWrapper(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(SHIFT_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // additiveExpressionWrapper shiftExpression*
  static boolean shiftExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shiftExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = additiveExpressionWrapper(builder_, level_ + 1);
    result_ = result_ && shiftExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // shiftExpression*
  private static boolean shiftExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shiftExpressionWrapper_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!shiftExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "shiftExpressionWrapper_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // '<<' | shiftRightOperator
  public static boolean shiftOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shiftOperator")) return false;
    if (!nextTokenIs(builder_, LT_LT) && !nextTokenIs(builder_, GT)
        && replaceVariants(builder_, 2, "<shift operator>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<shift operator>");
    result_ = consumeToken(builder_, LT_LT);
    if (!result_) result_ = shiftRightOperator(builder_, level_ + 1);
    if (result_) {
      marker_.done(SHIFT_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // '>' '>'
  public static boolean shiftRightOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shiftRightOperator")) return false;
    if (!nextTokenIs(builder_, GT)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, GT);
    result_ = result_ && consumeToken(builder_, GT);
    if (result_) {
      marker_.done(SHIFT_RIGHT_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // SHORT_TEMPLATE_ENTRY_START (thisExpression | referenceExpression)
  public static boolean shortTemplateEntry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shortTemplateEntry")) return false;
    if (!nextTokenIs(builder_, SHORT_TEMPLATE_ENTRY_START)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, SHORT_TEMPLATE_ENTRY_START);
    pinned_ = result_; // pin = 1
    result_ = result_ && shortTemplateEntry_1(builder_, level_ + 1);
    if (result_ || pinned_) {
      marker_.done(SHORT_TEMPLATE_ENTRY);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // thisExpression | referenceExpression
  private static boolean shortTemplateEntry_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shortTemplateEntry_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = thisExpression(builder_, level_ + 1);
    if (!result_) result_ = referenceExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'show' libraryReferenceList
  public static boolean showCombinator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "showCombinator")) return false;
    if (!nextTokenIs(builder_, SHOW)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SHOW);
    result_ = result_ && libraryReferenceList(builder_, level_ + 1);
    if (result_) {
      marker_.done(SHOW_COMBINATOR);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // referenceExpression qualifiedReferenceExpression*
  public static boolean simpleQualifiedReferenceExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simpleQualifiedReferenceExpression")) return false;
    boolean result_ = false;
    int start_ = builder_.getCurrentOffset();
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<simple qualified reference expression>");
    result_ = referenceExpression(builder_, level_ + 1);
    result_ = result_ && simpleQualifiedReferenceExpression_1(builder_, level_ + 1);
    LighterASTNode last_ = result_? builder_.getLatestDoneMarker() : null;
    if (last_ != null && last_.getStartOffset() == start_ && type_extends_(last_.getTokenType(), REFERENCE_EXPRESSION)) {
      marker_.drop();
    }
    else if (result_) {
      marker_.done(REFERENCE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // qualifiedReferenceExpression*
  private static boolean simpleQualifiedReferenceExpression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simpleQualifiedReferenceExpression_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!qualifiedReferenceExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "simpleQualifiedReferenceExpression_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // !'}'
  static boolean simple_scope_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simple_scope_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !consumeToken(builder_, RBRACE);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  /* ********************************************************** */
  // '#' 'source' '(' pathOrLibraryReference ')' ';'
  static boolean source1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "source1")) return false;
    if (!nextTokenIs(builder_, HASH)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, HASH);
    result_ = result_ && consumeToken(builder_, SOURCE);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, consumeToken(builder_, LPAREN));
    result_ = pinned_ && report_error_(builder_, pathOrLibraryReference(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RPAREN)) && result_;
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'part' pathOrLibraryReference ';'
  static boolean source2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "source2")) return false;
    if (!nextTokenIs(builder_, PART)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, PART);
    result_ = result_ && pathOrLibraryReference(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // source1 | source2
  public static boolean sourceStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sourceStatement")) return false;
    if (!nextTokenIs(builder_, HASH) && !nextTokenIs(builder_, PART)
        && replaceVariants(builder_, 2, "<source statement>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<source statement>");
    result_ = source1(builder_, level_ + 1);
    if (!result_) result_ = source2(builder_, level_ + 1);
    if (result_) {
      marker_.done(SOURCE_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // label* nonLabelledStatement
  static boolean statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, null);
    result_ = statement_0(builder_, level_ + 1);
    result_ = result_ && nonLabelledStatement(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_RECOVER_, statement_recover_parser_);
    return result_ || pinned_;
  }

  // label*
  private static boolean statement_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!label(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "statement_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // (metadata* varDeclarationList | expression) ';'
  static boolean statementFollowedBySemiColon(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementFollowedBySemiColon")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = statementFollowedBySemiColon_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // metadata* varDeclarationList | expression
  private static boolean statementFollowedBySemiColon_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementFollowedBySemiColon_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = statementFollowedBySemiColon_0_0(builder_, level_ + 1);
    if (!result_) result_ = expression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // metadata* varDeclarationList
  private static boolean statementFollowedBySemiColon_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementFollowedBySemiColon_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = statementFollowedBySemiColon_0_0_0(builder_, level_ + 1);
    result_ = result_ && varDeclarationList(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // metadata*
  private static boolean statementFollowedBySemiColon_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementFollowedBySemiColon_0_0_0")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!metadata(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "statementFollowedBySemiColon_0_0_0");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // !('!' | '(' | ')' | '+' | '++' | '-' | '--' | ';' | '<' | '[' | 'assert' | 'break' | 'case' | 'const'
  //                               | 'continue' | 'default' | 'do' | 'else' | 'false' | 'final' | 'for' | 'if' | 'new' | 'null' | 'return'
  //                               | 'static' | 'super' | 'switch' | 'this' | 'throw' | 'true' | 'try' | 'var' | 'while' | '{' | '}' | '~'
  //                               | HEX_NUMBER | <<nonStrictID>> | NUMBER | OPEN_QUOTE | RAW_SINGLE_QUOTED_STRING | RAW_TRIPLE_QUOTED_STRING
  //                               | "abstract" | "assert" | "class"  | "extends" | "factory" | "get" | "implements" | "import" | "interface"
  //                               | "is" | "@" | "library" | "native" | "negate" | "as" | "on" | "set" | "source" | "static" | "typedef" | "operator")
  static boolean statement_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !statement_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // '!' | '(' | ')' | '+' | '++' | '-' | '--' | ';' | '<' | '[' | 'assert' | 'break' | 'case' | 'const'
  //                               | 'continue' | 'default' | 'do' | 'else' | 'false' | 'final' | 'for' | 'if' | 'new' | 'null' | 'return'
  //                               | 'static' | 'super' | 'switch' | 'this' | 'throw' | 'true' | 'try' | 'var' | 'while' | '{' | '}' | '~'
  //                               | HEX_NUMBER | <<nonStrictID>> | NUMBER | OPEN_QUOTE | RAW_SINGLE_QUOTED_STRING | RAW_TRIPLE_QUOTED_STRING
  //                               | "abstract" | "assert" | "class"  | "extends" | "factory" | "get" | "implements" | "import" | "interface"
  //                               | "is" | "@" | "library" | "native" | "negate" | "as" | "on" | "set" | "source" | "static" | "typedef" | "operator"
  private static boolean statement_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, NOT);
    if (!result_) result_ = consumeToken(builder_, LPAREN);
    if (!result_) result_ = consumeToken(builder_, RPAREN);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, PLUS_PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, MINUS_MINUS);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = consumeToken(builder_, LT);
    if (!result_) result_ = consumeToken(builder_, LBRACKET);
    if (!result_) result_ = consumeToken(builder_, ASSERT);
    if (!result_) result_ = consumeToken(builder_, BREAK);
    if (!result_) result_ = consumeToken(builder_, CASE);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) result_ = consumeToken(builder_, CONTINUE);
    if (!result_) result_ = consumeToken(builder_, DEFAULT);
    if (!result_) result_ = consumeToken(builder_, DO);
    if (!result_) result_ = consumeToken(builder_, ELSE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    if (!result_) result_ = consumeToken(builder_, FINAL);
    if (!result_) result_ = consumeToken(builder_, FOR);
    if (!result_) result_ = consumeToken(builder_, IF);
    if (!result_) result_ = consumeToken(builder_, NEW);
    if (!result_) result_ = consumeToken(builder_, NULL);
    if (!result_) result_ = consumeToken(builder_, RETURN);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, SUPER);
    if (!result_) result_ = consumeToken(builder_, SWITCH);
    if (!result_) result_ = consumeToken(builder_, THIS);
    if (!result_) result_ = consumeToken(builder_, THROW);
    if (!result_) result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, TRY);
    if (!result_) result_ = consumeToken(builder_, VAR);
    if (!result_) result_ = consumeToken(builder_, WHILE);
    if (!result_) result_ = consumeToken(builder_, LBRACE);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    if (!result_) result_ = consumeToken(builder_, BIN_NOT);
    if (!result_) result_ = consumeToken(builder_, HEX_NUMBER);
    if (!result_) result_ = nonStrictID(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, OPEN_QUOTE);
    if (!result_) result_ = consumeToken(builder_, RAW_SINGLE_QUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, RAW_TRIPLE_QUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, ABSTRACT);
    if (!result_) result_ = consumeToken(builder_, ASSERT);
    if (!result_) result_ = consumeToken(builder_, CLASS);
    if (!result_) result_ = consumeToken(builder_, EXTENDS);
    if (!result_) result_ = consumeToken(builder_, FACTORY);
    if (!result_) result_ = consumeToken(builder_, GET);
    if (!result_) result_ = consumeToken(builder_, IMPLEMENTS);
    if (!result_) result_ = consumeToken(builder_, IMPORT);
    if (!result_) result_ = consumeToken(builder_, INTERFACE);
    if (!result_) result_ = consumeToken(builder_, IS);
    if (!result_) result_ = consumeToken(builder_, AT);
    if (!result_) result_ = consumeToken(builder_, LIBRARY);
    if (!result_) result_ = consumeToken(builder_, NATIVE);
    if (!result_) result_ = consumeToken(builder_, NEGATE);
    if (!result_) result_ = consumeToken(builder_, AS);
    if (!result_) result_ = consumeToken(builder_, ON);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, SOURCE);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, TYPEDEF);
    if (!result_) result_ = consumeToken(builder_, OPERATOR);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // statement*
  public static boolean statements(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statements")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<statements>");
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!statement(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "statements");
        break;
      }
      offset_ = next_offset_;
    }
    marker_.done(STATEMENTS);
    exitErrorRecordingSection(builder_, level_, true, false, _SECTION_GENERAL_, null);
    return true;
  }

  /* ********************************************************** */
  // (RAW_SINGLE_QUOTED_STRING | RAW_TRIPLE_QUOTED_STRING | stringTemplate)+
  public static boolean stringLiteralExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stringLiteralExpression")) return false;
    if (!nextTokenIs(builder_, OPEN_QUOTE) && !nextTokenIs(builder_, RAW_SINGLE_QUOTED_STRING)
        && !nextTokenIs(builder_, RAW_TRIPLE_QUOTED_STRING) && replaceVariants(builder_, 3, "<string literal expression>")) return false;
    boolean result_ = false;
    int start_ = builder_.getCurrentOffset();
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<string literal expression>");
    result_ = stringLiteralExpression_0(builder_, level_ + 1);
    int offset_ = builder_.getCurrentOffset();
    while (result_) {
      if (!stringLiteralExpression_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "stringLiteralExpression");
        break;
      }
      offset_ = next_offset_;
    }
    LighterASTNode last_ = result_? builder_.getLatestDoneMarker() : null;
    if (last_ != null && last_.getStartOffset() == start_ && type_extends_(last_.getTokenType(), STRING_LITERAL_EXPRESSION)) {
      marker_.drop();
    }
    else if (result_) {
      marker_.done(STRING_LITERAL_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // RAW_SINGLE_QUOTED_STRING | RAW_TRIPLE_QUOTED_STRING | stringTemplate
  private static boolean stringLiteralExpression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stringLiteralExpression_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, RAW_SINGLE_QUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, RAW_TRIPLE_QUOTED_STRING);
    if (!result_) result_ = stringTemplate(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // OPEN_QUOTE (REGULAR_STRING_PART | shortTemplateEntry | longTemplateEntry)* CLOSING_QUOTE
  static boolean stringTemplate(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stringTemplate")) return false;
    if (!nextTokenIs(builder_, OPEN_QUOTE)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, OPEN_QUOTE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, stringTemplate_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, CLOSING_QUOTE) && result_;
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // (REGULAR_STRING_PART | shortTemplateEntry | longTemplateEntry)*
  private static boolean stringTemplate_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stringTemplate_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!stringTemplate_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "stringTemplate_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // REGULAR_STRING_PART | shortTemplateEntry | longTemplateEntry
  private static boolean stringTemplate_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stringTemplate_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, REGULAR_STRING_PART);
    if (!result_) result_ = shortTemplateEntry(builder_, level_ + 1);
    if (!result_) result_ = longTemplateEntry(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '--' | '++'
  public static boolean suffixExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "suffixExpression")) return false;
    if (!nextTokenIs(builder_, PLUS_PLUS) && !nextTokenIs(builder_, MINUS_MINUS)
        && replaceVariants(builder_, 2, "<suffix expression>")) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "suffixExpression")) return false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<suffix expression>");
    result_ = consumeToken(builder_, MINUS_MINUS);
    if (!result_) result_ = consumeToken(builder_, PLUS_PLUS);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(SUFFIX_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // valueExpression suffixExpression*
  static boolean suffixExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "suffixExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = valueExpression(builder_, level_ + 1);
    result_ = result_ && suffixExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // suffixExpression*
  private static boolean suffixExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "suffixExpressionWrapper_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!suffixExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "suffixExpressionWrapper_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // ('super' | 'this') ('.' referenceExpression)? arguments
  //                               | fieldInitializer
  public static boolean superCallOrFieldInitializer(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "superCallOrFieldInitializer")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<super call or field initializer>");
    result_ = superCallOrFieldInitializer_0(builder_, level_ + 1);
    if (!result_) result_ = fieldInitializer(builder_, level_ + 1);
    if (result_) {
      marker_.done(SUPER_CALL_OR_FIELD_INITIALIZER);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, super_call_or_field_initializer_recover_parser_);
    return result_;
  }

  // ('super' | 'this') ('.' referenceExpression)? arguments
  private static boolean superCallOrFieldInitializer_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "superCallOrFieldInitializer_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = superCallOrFieldInitializer_0_0(builder_, level_ + 1);
    result_ = result_ && superCallOrFieldInitializer_0_1(builder_, level_ + 1);
    result_ = result_ && arguments(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // 'super' | 'this'
  private static boolean superCallOrFieldInitializer_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "superCallOrFieldInitializer_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SUPER);
    if (!result_) result_ = consumeToken(builder_, THIS);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ('.' referenceExpression)?
  private static boolean superCallOrFieldInitializer_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "superCallOrFieldInitializer_0_1")) return false;
    superCallOrFieldInitializer_0_1_0(builder_, level_ + 1);
    return true;
  }

  // '.' referenceExpression
  private static boolean superCallOrFieldInitializer_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "superCallOrFieldInitializer_0_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && referenceExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'super'
  public static boolean superExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "superExpression")) return false;
    if (!nextTokenIs(builder_, SUPER)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SUPER);
    if (result_) {
      marker_.done(SUPER_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // !(')' | ',' | ':' | ';' | '=' | '=>' | ']' | 'abstract' | 'const' | 'factory'
  //                                                     | 'final' | 'get' | 'native' | 'operator' | 'set' | 'static' | 'var' | '{' | '}'
  //                                                     | <<nonStrictID>>)
  static boolean super_call_or_field_initializer_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "super_call_or_field_initializer_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !super_call_or_field_initializer_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // ')' | ',' | ':' | ';' | '=' | '=>' | ']' | 'abstract' | 'const' | 'factory'
  //                                                     | 'final' | 'get' | 'native' | 'operator' | 'set' | 'static' | 'var' | '{' | '}'
  //                                                     | <<nonStrictID>>
  private static boolean super_call_or_field_initializer_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "super_call_or_field_initializer_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, RPAREN);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = consumeToken(builder_, EQ);
    if (!result_) result_ = consumeToken(builder_, EXPRESSION_BODY_DEF);
    if (!result_) result_ = consumeToken(builder_, RBRACKET);
    if (!result_) result_ = consumeToken(builder_, ABSTRACT);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) result_ = consumeToken(builder_, FACTORY);
    if (!result_) result_ = consumeToken(builder_, FINAL);
    if (!result_) result_ = consumeToken(builder_, GET);
    if (!result_) result_ = consumeToken(builder_, NATIVE);
    if (!result_) result_ = consumeToken(builder_, OPERATOR);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, VAR);
    if (!result_) result_ = consumeToken(builder_, LBRACE);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    if (!result_) result_ = nonStrictID(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'extends' type
  public static boolean superclass(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "superclass")) return false;
    if (!nextTokenIs(builder_, EXTENDS)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, EXTENDS);
    pinned_ = result_; // pin = 1
    result_ = result_ && type(builder_, level_ + 1);
    if (result_ || pinned_) {
      marker_.done(SUPERCLASS);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'extends' typeList
  public static boolean superinterfaces(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "superinterfaces")) return false;
    if (!nextTokenIs(builder_, EXTENDS)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, EXTENDS);
    pinned_ = result_; // pin = 1
    result_ = result_ && typeList(builder_, level_ + 1);
    if (result_ || pinned_) {
      marker_.done(SUPERINTERFACES);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // label? ('case' expression ':')+ statements
  public static boolean switchCase(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switchCase")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<switch case>");
    result_ = switchCase_0(builder_, level_ + 1);
    result_ = result_ && switchCase_1(builder_, level_ + 1);
    result_ = result_ && statements(builder_, level_ + 1);
    if (result_) {
      marker_.done(SWITCH_CASE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, switch_case_recover_parser_);
    return result_;
  }

  // label?
  private static boolean switchCase_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switchCase_0")) return false;
    label(builder_, level_ + 1);
    return true;
  }

  // ('case' expression ':')+
  private static boolean switchCase_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switchCase_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = switchCase_1_0(builder_, level_ + 1);
    int offset_ = builder_.getCurrentOffset();
    while (result_) {
      if (!switchCase_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "switchCase_1");
        break;
      }
      offset_ = next_offset_;
    }
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // 'case' expression ':'
  private static boolean switchCase_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switchCase_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, CASE);
    result_ = result_ && expression(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'switch' '(' expressionWithRecoverUntilParen ')' '{' switchCase* defaultCase? '}'
  public static boolean switchStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switchStatement")) return false;
    if (!nextTokenIs(builder_, SWITCH)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, SWITCH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, consumeToken(builder_, LPAREN));
    result_ = pinned_ && report_error_(builder_, expressionWithRecoverUntilParen(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RPAREN)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, LBRACE)) && result_;
    result_ = pinned_ && report_error_(builder_, switchStatement_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, switchStatement_6(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, RBRACE) && result_;
    if (result_ || pinned_) {
      marker_.done(SWITCH_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // switchCase*
  private static boolean switchStatement_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switchStatement_5")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!switchCase(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "switchStatement_5");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // defaultCase?
  private static boolean switchStatement_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switchStatement_6")) return false;
    defaultCase(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // !('case' | 'default' | '}' | <<nonStrictID>>)
  static boolean switch_case_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_case_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !switch_case_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // 'case' | 'default' | '}' | <<nonStrictID>>
  private static boolean switch_case_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_case_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, CASE);
    if (!result_) result_ = consumeToken(builder_, DEFAULT);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    if (!result_) result_ = nonStrictID(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '?' expression ':' ternaryExpressionWrapper
  public static boolean ternaryExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ternaryExpression")) return false;
    if (!nextTokenIs(builder_, QUEST)) return false;
    boolean result_ = false;
    Marker left_marker_ = (Marker)builder_.getLatestDoneMarker();
    if (!invalid_left_marker_guard_(builder_, left_marker_, "ternaryExpression")) return false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, QUEST);
    result_ = result_ && expression(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && ternaryExpressionWrapper(builder_, level_ + 1);
    if (result_) {
      marker_.drop();
      left_marker_.precede().done(TERNARY_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // logicOrExpressionWrapper ternaryExpression?
  static boolean ternaryExpressionWrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ternaryExpressionWrapper")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = logicOrExpressionWrapper(builder_, level_ + 1);
    result_ = result_ && ternaryExpressionWrapper_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // ternaryExpression?
  private static boolean ternaryExpressionWrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ternaryExpressionWrapper_1")) return false;
    ternaryExpression(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'this'
  public static boolean thisExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "thisExpression")) return false;
    if (!nextTokenIs(builder_, THIS)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, THIS);
    if (result_) {
      marker_.done(THIS_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'throw' expression? ';'
  public static boolean throwStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "throwStatement")) return false;
    if (!nextTokenIs(builder_, THROW)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, THROW);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, throwStatement_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, SEMICOLON) && result_;
    if (result_ || pinned_) {
      marker_.done(THROW_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // expression?
  private static boolean throwStatement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "throwStatement_1")) return false;
    expression(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // libraryStatement
  //                              | partOfStatement
  //                              | importStatement
  //                              | exportStatement
  //                              | sourceStatement
  //                              | nativeStatement
  //                              | resourceStatement
  //                              | classDefinition
  //                              | interfaceDefinition
  //                              | functionTypeAlias
  //                              | functionDeclarationWithBodyOrNative
  //                              | getterOrSetterDeclaration
  //                              | varDeclarationListWithSemicolon
  static boolean topLevelDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "topLevelDefinition")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, null);
    result_ = libraryStatement(builder_, level_ + 1);
    if (!result_) result_ = partOfStatement(builder_, level_ + 1);
    if (!result_) result_ = importStatement(builder_, level_ + 1);
    if (!result_) result_ = exportStatement(builder_, level_ + 1);
    if (!result_) result_ = sourceStatement(builder_, level_ + 1);
    if (!result_) result_ = nativeStatement(builder_, level_ + 1);
    if (!result_) result_ = resourceStatement(builder_, level_ + 1);
    if (!result_) result_ = classDefinition(builder_, level_ + 1);
    if (!result_) result_ = interfaceDefinition(builder_, level_ + 1);
    if (!result_) result_ = functionTypeAlias(builder_, level_ + 1);
    if (!result_) result_ = functionDeclarationWithBodyOrNative(builder_, level_ + 1);
    if (!result_) result_ = getterOrSetterDeclaration(builder_, level_ + 1);
    if (!result_) result_ = varDeclarationListWithSemicolon(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, top_level_recover_parser_);
    return result_;
  }

  /* ********************************************************** */
  // !('#' | '@' | 'import' | 'library' | 'source' | 'native' | 'resource' | 'class' | 'const' | 'final' | 'get' | 'interface'
  //                               | 'set' | 'static' | 'typedef' | 'var' | <<nonStrictID>> | 'abstract' | 'part'| 'export' | 'external')
  static boolean top_level_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_level_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !top_level_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // '#' | '@' | 'import' | 'library' | 'source' | 'native' | 'resource' | 'class' | 'const' | 'final' | 'get' | 'interface'
  //                               | 'set' | 'static' | 'typedef' | 'var' | <<nonStrictID>> | 'abstract' | 'part'| 'export' | 'external'
  private static boolean top_level_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_level_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, HASH);
    if (!result_) result_ = consumeToken(builder_, AT);
    if (!result_) result_ = consumeToken(builder_, IMPORT);
    if (!result_) result_ = consumeToken(builder_, LIBRARY);
    if (!result_) result_ = consumeToken(builder_, SOURCE);
    if (!result_) result_ = consumeToken(builder_, NATIVE);
    if (!result_) result_ = consumeToken(builder_, RESOURCE);
    if (!result_) result_ = consumeToken(builder_, CLASS);
    if (!result_) result_ = consumeToken(builder_, CONST);
    if (!result_) result_ = consumeToken(builder_, FINAL);
    if (!result_) result_ = consumeToken(builder_, GET);
    if (!result_) result_ = consumeToken(builder_, INTERFACE);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, TYPEDEF);
    if (!result_) result_ = consumeToken(builder_, VAR);
    if (!result_) result_ = nonStrictID(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, ABSTRACT);
    if (!result_) result_ = consumeToken(builder_, PART);
    if (!result_) result_ = consumeToken(builder_, EXPORT);
    if (!result_) result_ = consumeToken(builder_, EXTERNAL);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'try' block ((onPart | catchPart)+ finallyPart? | finallyPart)
  public static boolean tryStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tryStatement")) return false;
    if (!nextTokenIs(builder_, TRY)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, TRY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, block(builder_, level_ + 1));
    result_ = pinned_ && tryStatement_2(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(TRY_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // (onPart | catchPart)+ finallyPart? | finallyPart
  private static boolean tryStatement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tryStatement_2")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = tryStatement_2_0(builder_, level_ + 1);
    if (!result_) result_ = finallyPart(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (onPart | catchPart)+ finallyPart?
  private static boolean tryStatement_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tryStatement_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = tryStatement_2_0_0(builder_, level_ + 1);
    result_ = result_ && tryStatement_2_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // (onPart | catchPart)+
  private static boolean tryStatement_2_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tryStatement_2_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = tryStatement_2_0_0_0(builder_, level_ + 1);
    int offset_ = builder_.getCurrentOffset();
    while (result_) {
      if (!tryStatement_2_0_0_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "tryStatement_2_0_0");
        break;
      }
      offset_ = next_offset_;
    }
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // onPart | catchPart
  private static boolean tryStatement_2_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tryStatement_2_0_0_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = onPart(builder_, level_ + 1);
    if (!result_) result_ = catchPart(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // finallyPart?
  private static boolean tryStatement_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tryStatement_2_0_1")) return false;
    finallyPart(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // simpleQualifiedReferenceExpression typeArguments?
  public static boolean type(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<type>");
    result_ = simpleQualifiedReferenceExpression(builder_, level_ + 1);
    result_ = result_ && type_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(TYPE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // typeArguments?
  private static boolean type_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_1")) return false;
    typeArguments(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '<' typeList '>'
  public static boolean typeArguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeArguments")) return false;
    if (!nextTokenIs(builder_, LT)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LT);
    result_ = result_ && typeList(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, GT);
    if (result_) {
      marker_.done(TYPE_ARGUMENTS);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // type (',' type)*
  public static boolean typeList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeList")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<type list>");
    result_ = type(builder_, level_ + 1);
    result_ = result_ && typeList_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(TYPE_LIST);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // (',' type)*
  private static boolean typeList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeList_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!typeList_1_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "typeList_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' type
  private static boolean typeList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeList_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && type(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // componentName ('extends' type)?
  public static boolean typeParameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeParameter")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_RECOVER_, "<type parameter>");
    result_ = componentName(builder_, level_ + 1);
    result_ = result_ && typeParameter_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(TYPE_PARAMETER);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_RECOVER_, type_parameter_recover_parser_);
    return result_;
  }

  // ('extends' type)?
  private static boolean typeParameter_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeParameter_1")) return false;
    typeParameter_1_0(builder_, level_ + 1);
    return true;
  }

  // 'extends' type
  private static boolean typeParameter_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeParameter_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, EXTENDS);
    result_ = result_ && type(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // '<' typeParameter (',' typeParameter)* '>'
  public static boolean typeParameters(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeParameters")) return false;
    if (!nextTokenIs(builder_, LT)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, LT);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, typeParameter(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, typeParameters_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, GT) && result_;
    if (result_ || pinned_) {
      marker_.done(TYPE_PARAMETERS);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // (',' typeParameter)*
  private static boolean typeParameters_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeParameters_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!typeParameters_2_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "typeParameters_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' typeParameter
  private static boolean typeParameters_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeParameters_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && typeParameter(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // !('(' | ',' | '.' | '>' | 'extends' | 'factory' | 'implements' | '{')
  static boolean type_parameter_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_parameter_recover")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_NOT_, null);
    result_ = !type_parameter_recover_0(builder_, level_ + 1);
    marker_.rollbackTo();
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_NOT_, null);
    return result_;
  }

  // '(' | ',' | '.' | '>' | 'extends' | 'factory' | 'implements' | '{'
  private static boolean type_parameter_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_parameter_recover_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LPAREN);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, GT);
    if (!result_) result_ = consumeToken(builder_, EXTENDS);
    if (!result_) result_ = consumeToken(builder_, FACTORY);
    if (!result_) result_ = consumeToken(builder_, IMPLEMENTS);
    if (!result_) result_ = consumeToken(builder_, LBRACE);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // multiplicativeOperator
  //                         | additiveOperator
  //                         | shiftOperator
  //                         | relationalOperator
  //                         | bitwiseOperator
  //                         | '=='  // Disallow negative '&' === equality checks.
  //                         | '~'   // Disallow ! operator.
  //                         | 'negate'
  //                         | '[' ']' '='?
  public static boolean userDefinableOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "userDefinableOperator")) return false;
    if (!nextTokenIs(builder_, REM) && !nextTokenIs(builder_, AND)
        && !nextTokenIs(builder_, MUL) && !nextTokenIs(builder_, PLUS)
        && !nextTokenIs(builder_, MINUS) && !nextTokenIs(builder_, DIV)
        && !nextTokenIs(builder_, LT) && !nextTokenIs(builder_, LT_LT)
        && !nextTokenIs(builder_, LT_EQ) && !nextTokenIs(builder_, EQ_EQ)
        && !nextTokenIs(builder_, GT) && !nextTokenIs(builder_, GT_EQ)
        && !nextTokenIs(builder_, LBRACKET) && !nextTokenIs(builder_, XOR)
        && !nextTokenIs(builder_, NEGATE) && !nextTokenIs(builder_, OR)
        && !nextTokenIs(builder_, BIN_NOT) && !nextTokenIs(builder_, INT_DIV)
        && replaceVariants(builder_, 18, "<user definable operator>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<user definable operator>");
    result_ = multiplicativeOperator(builder_, level_ + 1);
    if (!result_) result_ = additiveOperator(builder_, level_ + 1);
    if (!result_) result_ = shiftOperator(builder_, level_ + 1);
    if (!result_) result_ = relationalOperator(builder_, level_ + 1);
    if (!result_) result_ = bitwiseOperator(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, EQ_EQ);
    if (!result_) result_ = consumeToken(builder_, BIN_NOT);
    if (!result_) result_ = consumeToken(builder_, NEGATE);
    if (!result_) result_ = userDefinableOperator_8(builder_, level_ + 1);
    if (result_) {
      marker_.done(USER_DEFINABLE_OPERATOR);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // '[' ']' '='?
  private static boolean userDefinableOperator_8(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "userDefinableOperator_8")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    result_ = result_ && userDefinableOperator_8_2(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // '='?
  private static boolean userDefinableOperator_8_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "userDefinableOperator_8_2")) return false;
    consumeToken(builder_, EQ);
    return true;
  }

  /* ********************************************************** */
  // (literalExpression qualifiedReferenceTail?)
  //                  | functionExpression
  //                  | compoundLiteralExpression
  //                  | newExpressionOrConstOrCall cascadeReferenceExpression*
  //                  | callOrArrayAccess cascadeReferenceExpression*
  static boolean value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = value_0(builder_, level_ + 1);
    if (!result_) result_ = functionExpression(builder_, level_ + 1);
    if (!result_) result_ = compoundLiteralExpression(builder_, level_ + 1);
    if (!result_) result_ = value_3(builder_, level_ + 1);
    if (!result_) result_ = value_4(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // literalExpression qualifiedReferenceTail?
  private static boolean value_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = literalExpression(builder_, level_ + 1);
    result_ = result_ && value_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // qualifiedReferenceTail?
  private static boolean value_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value_0_1")) return false;
    qualifiedReferenceTail(builder_, level_ + 1);
    return true;
  }

  // newExpressionOrConstOrCall cascadeReferenceExpression*
  private static boolean value_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value_3")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = newExpressionOrConstOrCall(builder_, level_ + 1);
    result_ = result_ && value_3_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // cascadeReferenceExpression*
  private static boolean value_3_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value_3_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!cascadeReferenceExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "value_3_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // callOrArrayAccess cascadeReferenceExpression*
  private static boolean value_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value_4")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = callOrArrayAccess(builder_, level_ + 1);
    result_ = result_ && value_4_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // cascadeReferenceExpression*
  private static boolean value_4_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value_4_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!cascadeReferenceExpression(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "value_4_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // value (isExpression | asExpression)?
  public static boolean valueExpression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "valueExpression")) return false;
    boolean result_ = false;
    int start_ = builder_.getCurrentOffset();
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<value expression>");
    result_ = value(builder_, level_ + 1);
    result_ = result_ && valueExpression_1(builder_, level_ + 1);
    LighterASTNode last_ = result_? builder_.getLatestDoneMarker() : null;
    if (last_ != null && last_.getStartOffset() == start_ && type_extends_(last_.getTokenType(), VALUE_EXPRESSION)) {
      marker_.drop();
    }
    else if (result_) {
      marker_.done(VALUE_EXPRESSION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // (isExpression | asExpression)?
  private static boolean valueExpression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "valueExpression_1")) return false;
    valueExpression_1_0(builder_, level_ + 1);
    return true;
  }

  // isExpression | asExpression
  private static boolean valueExpression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "valueExpression_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = isExpression(builder_, level_ + 1);
    if (!result_) result_ = asExpression(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // 'static'? (finalOrConst type componentName | finalOrConst componentName | type componentName | 'var' componentName)
  public static boolean varAccessDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varAccessDeclaration")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<var access declaration>");
    result_ = varAccessDeclaration_0(builder_, level_ + 1);
    result_ = result_ && varAccessDeclaration_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(VAR_ACCESS_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // 'static'?
  private static boolean varAccessDeclaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varAccessDeclaration_0")) return false;
    consumeToken(builder_, STATIC);
    return true;
  }

  // finalOrConst type componentName | finalOrConst componentName | type componentName | 'var' componentName
  private static boolean varAccessDeclaration_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varAccessDeclaration_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = varAccessDeclaration_1_0(builder_, level_ + 1);
    if (!result_) result_ = varAccessDeclaration_1_1(builder_, level_ + 1);
    if (!result_) result_ = varAccessDeclaration_1_2(builder_, level_ + 1);
    if (!result_) result_ = varAccessDeclaration_1_3(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // finalOrConst type componentName
  private static boolean varAccessDeclaration_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varAccessDeclaration_1_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = finalOrConst(builder_, level_ + 1);
    result_ = result_ && type(builder_, level_ + 1);
    result_ = result_ && componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // finalOrConst componentName
  private static boolean varAccessDeclaration_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varAccessDeclaration_1_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = finalOrConst(builder_, level_ + 1);
    result_ = result_ && componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // type componentName
  private static boolean varAccessDeclaration_1_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varAccessDeclaration_1_2")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = type(builder_, level_ + 1);
    result_ = result_ && componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // 'var' componentName
  private static boolean varAccessDeclaration_1_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varAccessDeclaration_1_3")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, VAR);
    result_ = result_ && componentName(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // varAccessDeclaration varInit?
  public static boolean varDeclaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varDeclaration")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<var declaration>");
    result_ = varAccessDeclaration(builder_, level_ + 1);
    result_ = result_ && varDeclaration_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(VAR_DECLARATION);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // varInit?
  private static boolean varDeclaration_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varDeclaration_1")) return false;
    varInit(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // varAccessDeclaration varInit? (',' varDeclarationListPart)*
  public static boolean varDeclarationList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varDeclarationList")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<var declaration list>");
    result_ = varAccessDeclaration(builder_, level_ + 1);
    result_ = result_ && varDeclarationList_1(builder_, level_ + 1);
    result_ = result_ && varDeclarationList_2(builder_, level_ + 1);
    pinned_ = result_; // pin = 3
    if (result_ || pinned_) {
      marker_.done(VAR_DECLARATION_LIST);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  // varInit?
  private static boolean varDeclarationList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varDeclarationList_1")) return false;
    varInit(builder_, level_ + 1);
    return true;
  }

  // (',' varDeclarationListPart)*
  private static boolean varDeclarationList_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varDeclarationList_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!varDeclarationList_2_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "varDeclarationList_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // ',' varDeclarationListPart
  private static boolean varDeclarationList_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varDeclarationList_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && varDeclarationListPart(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // componentName varInit?
  public static boolean varDeclarationListPart(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varDeclarationListPart")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<var declaration list part>");
    result_ = componentName(builder_, level_ + 1);
    result_ = result_ && varDeclarationListPart_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(VAR_DECLARATION_LIST_PART);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // varInit?
  private static boolean varDeclarationListPart_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varDeclarationListPart_1")) return false;
    varInit(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // varDeclarationList ';'
  static boolean varDeclarationListWithSemicolon(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varDeclarationListWithSemicolon")) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = varDeclarationList(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    if (!result_ && !pinned_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '=' expression
  public static boolean varInit(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "varInit")) return false;
    if (!nextTokenIs(builder_, EQ)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, EQ);
    pinned_ = result_; // pin = 1
    result_ = result_ && expression(builder_, level_ + 1);
    if (result_ || pinned_) {
      marker_.done(VAR_INIT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'while' '(' expressionWithRecoverUntilParen ')' statement
  public static boolean whileStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "whileStatement")) return false;
    if (!nextTokenIs(builder_, WHILE)) return false;
    boolean result_ = false;
    boolean pinned_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, null);
    result_ = consumeToken(builder_, WHILE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, consumeToken(builder_, LPAREN));
    result_ = pinned_ && report_error_(builder_, expressionWithRecoverUntilParen(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RPAREN)) && result_;
    result_ = pinned_ && statement(builder_, level_ + 1) && result_;
    if (result_ || pinned_) {
      marker_.done(WHILE_STATEMENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, pinned_, _SECTION_GENERAL_, null);
    return result_ || pinned_;
  }

  final static Parser argument_list_part_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return argument_list_part_recover(builder_, level_ + 1);
    }
  };
  final static Parser argument_list_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return argument_list_recover(builder_, level_ + 1);
    }
  };
  final static Parser class_member_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return class_member_recover(builder_, level_ + 1);
    }
  };
  final static Parser default_formal_parameter_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return default_formal_parameter_recover(builder_, level_ + 1);
    }
  };
  final static Parser expression_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return expression_recover(builder_, level_ + 1);
    }
  };
  final static Parser for_loops_parts_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return for_loops_parts_recover(builder_, level_ + 1);
    }
  };
  final static Parser interface_member_definition_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return interface_member_definition_recover(builder_, level_ + 1);
    }
  };
  final static Parser map_literal_entry_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return map_literal_entry_recover(builder_, level_ + 1);
    }
  };
  final static Parser not_paren_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return not_paren_recover(builder_, level_ + 1);
    }
  };
  final static Parser semicolon_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return semicolon_recover(builder_, level_ + 1);
    }
  };
  final static Parser simple_scope_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return simple_scope_recover(builder_, level_ + 1);
    }
  };
  final static Parser statement_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return statement_recover(builder_, level_ + 1);
    }
  };
  final static Parser super_call_or_field_initializer_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return super_call_or_field_initializer_recover(builder_, level_ + 1);
    }
  };
  final static Parser switch_case_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return switch_case_recover(builder_, level_ + 1);
    }
  };
  final static Parser top_level_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return top_level_recover(builder_, level_ + 1);
    }
  };
  final static Parser type_parameter_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder_, int level_) {
      return type_parameter_recover(builder_, level_ + 1);
    }
  };
}
