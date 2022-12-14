package Helpers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Handheld {

    ArrayList<Instruction> rom;
    Integer instPoint = 0;
    public Long acc = 0L;

    public Handheld(ArrayList<Instruction> rom){
        this.rom = rom;
    }

    public boolean execute(){
        var executed = new HashSet<Integer>();
        while (instPoint >= 0 && instPoint < rom.size() ){
            if (executed.contains(instPoint)) return false;
            executed.add(instPoint);
            var instruction = rom.get(instPoint);
            switch (instruction.code()){
                case ACC -> {
                    acc += instruction.value();
                    instPoint++;
                }
                case NOP -> {
                    instPoint++;
                }
                case JMP -> {
                    instPoint += instruction.value();
                }
            }
        }
        return true;
    }

    public static @NotNull ArrayList<Instruction> parseInstructions(ArrayList<String> input){
        var output = new ArrayList<Instruction>();

        for (var line : input){
            var pieces = line.split(" ");
            var inst = OpCode.NOP;
            if (Objects.equals(pieces[0], "acc")) inst = OpCode.ACC;
            if (Objects.equals(pieces[0], "jmp")) inst = OpCode.JMP;
            output.add(new Instruction(inst, Integer.parseInt(pieces[1])));
        }

        return output;
    }

}

