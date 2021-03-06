package com.javadeobfuscator.javavm.instructions;

import com.javadeobfuscator.javavm.Locals;
import com.javadeobfuscator.javavm.MethodExecution;
import com.javadeobfuscator.javavm.Stack;
import com.javadeobfuscator.javavm.utils.MaybeBoolean;
import com.javadeobfuscator.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

import java.util.List;
import java.util.function.BiFunction;

public class DoublePredicateInstruction extends Instruction {

    private final BiFunction<JavaWrapper, JavaWrapper, MaybeBoolean> _handler;

    public DoublePredicateInstruction(BiFunction<JavaWrapper, JavaWrapper, MaybeBoolean> handler) {
        this._handler = handler;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        JumpInsnNode jumpInsnNode = (JumpInsnNode) currentInsn;

        JavaWrapper top = stack.pop();
        JavaWrapper bottom = stack.pop();
        MaybeBoolean result = _handler.apply(bottom, top);
        switch (result) {
            case YES:
                branchTo.add(jumpInsnNode.label);
                break;
            case MAYBE:
                branchTo.add(jumpInsnNode.label);
                branchTo.add(jumpInsnNode.getNext());
                break;
            case NO:
                break;
        }
    }
}
