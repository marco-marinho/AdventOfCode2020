package Day08;

import Helpers.Handheld;
import Helpers.Instruction;
import Helpers.OpCode;

import java.util.ArrayList;

import static Helpers.Handheld.parseInstructions;
import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {

        var data = readFile("Data/Day08.txt");
        var rom = parseInstructions(data);
        var handheld = new Handheld(rom);
        handheld.execute();
        out.println("Task 01: " + handheld.acc);

        for (var i = 0; i < rom.size(); i++){
            var tempRom = new ArrayList<>(rom);
            var curInst = tempRom.get(i);
            var curOpCode = curInst.code();
            if (curOpCode != OpCode.NOP && curOpCode != OpCode.JMP) continue;
            var newInst = new Instruction(OpCode.NOP, curInst.value());
            if (curOpCode == OpCode.NOP){
                newInst = new Instruction(OpCode.JMP, curInst.value());
            }
            tempRom.set(i, newInst);
            var tempHandHeld = new Handheld(tempRom);
            var res = tempHandHeld.execute();
            if (res) {
                out.println("Task 02: " + tempHandHeld.acc);
                break;
            }
        }

    }
}