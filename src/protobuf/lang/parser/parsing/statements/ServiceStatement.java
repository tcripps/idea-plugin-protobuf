package protobuf.lang.parser.parsing.statements;

import com.intellij.lang.PsiBuilder;
import protobuf.lang.ProtobufElementTypes;
import protobuf.lang.parser.parsing.ReferenceElement;
import protobuf.lang.parser.util.PatchedPsiBuilder;

/**
 * author: Nikolay Matveev
 * Date: Mar 10, 2010
 */

//  grammar - ok
//  PbServiceDef ::= 'service' IDENTIFIER serviceBlock
//  serviceBlock ::= '{' (serviceStatement | ';')* '}'
//  serviceStatement ::= serviceMethod | optionDefinition
//  serviceMethod ::= 'rpc' IDENTIFIER '(' userDefinedType ')' 'returns' '(' userDefinedType ')' serviceMethodBlock? ';'
//  serviceMethodBlock ::= '{' (optionDefinition|';')* '}'

public class ServiceStatement implements ProtobufElementTypes {
    public static boolean parse(PatchedPsiBuilder builder) {
        if (!builder.compareToken(SERVICE)) {
            return false;
        }
        PsiBuilder.Marker serviceMarker = builder.mark();
        builder.match(SERVICE);
        builder.match(IK, "identifier.expected");
        //builder.matchAs(IK, NAME, "identifier.expected");
        if (!parseServiceBlock(builder)) {
            builder.error("service.block.expected");
        }
        serviceMarker.done(SERVICE_DECL);
        return true;
    }

    //done

    public static boolean parseServiceBlock(PatchedPsiBuilder builder) {
        if (!builder.compareToken(OPEN_BLOCK)) {
            return false;
        }
        PsiBuilder.Marker serviceBlockMarker = builder.mark();
        builder.match(OPEN_BLOCK);
        while (!builder.eof() && !builder.compareToken(CLOSE_BLOCK)) {
            if (builder.match(SEMICOLON)) {
            } else if (parseServiceMethod(builder)) {
            } else if (OptionStatement.parseSeparateOption(builder)) {
            } else {
                builder.eatError("unexpected.token");
            }

        }
        builder.match(CLOSE_BLOCK, "close.block.expected");
        serviceBlockMarker.done(SERVICE_BLOCK);
        return true;
    }

    //done
    public static boolean parseServiceMethod(PatchedPsiBuilder builder) {
        if (!builder.compareToken(RPC)) {
            return false;
        }
        PsiBuilder.Marker serviceStatementMarker = builder.mark();
        builder.match(RPC);
        builder.match(IK, "identifier.expected");
        //builder.matchAs(IK,NAME, "identifier.expected");
        builder.match(OPEN_PARANT, "open.parant.expected");
        if (!ReferenceElement.parseForCustomType(builder)) {
            builder.error("user.defined.type.expected");
        }
        builder.match(CLOSE_PARANT, "close.parant.expected");
        builder.match(RETURNS, "returns.expected");
        builder.match(OPEN_PARANT, "open.parant.expected");
        if (!ReferenceElement.parseForCustomType(builder)) {
            builder.error("user.defined.type.expected");
        }
        builder.match(CLOSE_PARANT, "close.parant.expected");
        if(!parseServiceMethodBlock(builder)){
            builder.match(SEMICOLON,"semicolon.expected");    
        }
        serviceStatementMarker.done(SERVICE_METHOD_DECL);
        return true;
    }

    //done

    public static boolean parseServiceMethodBlock(PatchedPsiBuilder builder) {
        if (!builder.compareToken(OPEN_BLOCK)) {
            return false;
        }
        PsiBuilder.Marker serviceMethodBlockMarker = builder.mark();
        builder.match(OPEN_BLOCK);
        while (!builder.eof() && !builder.compareToken(CLOSE_BLOCK)) {
            if (builder.match(SEMICOLON)) {
            } else if (OptionStatement.parseSeparateOption(builder)) {
            } else {
                builder.eatError("unexpected.token");
            }
        }
        builder.match(CLOSE_BLOCK, "close.block.expected");
        serviceMethodBlockMarker.done(SERVICE_METHOD_BLOCK);
        return true;
    }
}

