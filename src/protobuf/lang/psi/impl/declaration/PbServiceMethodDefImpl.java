package protobuf.lang.psi.impl.declaration;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import protobuf.lang.psi.ProtobufPsiElementVisitor;
import protobuf.lang.psi.api.declaration.PbServiceMethodDef;
import protobuf.lang.psi.impl.auxiliary.PbNamedBlockHolderImpl;
import protobuf.lang.psi.utils.PbPsiUtil;

/**
 * author: Nikolay Matveev
 * Date: Mar 24, 2010
 */
public class PbServiceMethodDefImpl extends PbNamedBlockHolderImpl implements PbServiceMethodDef {
    public PbServiceMethodDefImpl(ASTNode node){
        super(node);
    }

    @Override
    public void accept(ProtobufPsiElementVisitor visitor) {
        visitor.visitServiceMethodDefinition(this);
    }

    @Override
    public PsiElement getNameElement() {
        return PbPsiUtil.getChild(this,1,true,true,false);
    }
}
