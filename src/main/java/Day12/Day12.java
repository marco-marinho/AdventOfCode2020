package Day12;

import Helpers.Point;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day12 {

    public static void main(String[] args){

        var data = readFile("Data/Day12.txt");
        var commands = new ArrayList<Command>();
        for (var line : data){
            var command = new Command(line.charAt(0), Integer.parseInt(line.substring(1)));
            commands.add(command);
        }

        var ship = new Ship();
        for (var command: commands){
            ship.Execute(command);
        }
        out.println("Task 01: " + ship.position.manhattan());

        var newship = new NewShip();
        for (var command: commands){
            newship.Execute(command);
        }
        out.println("Task 02: " + newship.position.manhattan());
    }

}

record Command(Character command, Integer value){}

class Ship{

    Character direction;
    Point position;

    public Ship(){
        direction = 'E';
        position = new Point(0 ,0);
    }

    public void Execute(@NotNull Command command){
        if (command.command() == 'N') position = new Point(position.x() - command.value(), position.y());
        else if (command.command() == 'S') position = new Point(position.x() + command.value(), position.y());
        else if (command.command() == 'E') position = new Point(position.x(), position.y() + command.value());
        else if (command.command() == 'W') position = new Point(position.x(), position.y() - command.value());
        else if (command.command() == 'R') Turn('R', command.value());
        else if (command.command() == 'L') Turn('L', command.value());
        else if (command.command() == 'F') Execute(new Command(direction, command.value()));
    }

    public void Turn(Character direction, Integer degrees){
        var directions = new Character[]{'N', 'E', 'S', 'W'};
        var turns = degrees / 90;
        var cur = ArrayUtils.indexOf(directions, this.direction);
        if (direction == 'L') turns *= -1;
        var next = cur + turns;
        if (next < 0) next = directions.length + next;
        else next = next % directions.length;
        this.direction = directions[next];
    }
}
class NewShip{

    Point position;

    Point waypoint;

    public NewShip(){
        position = new Point(0 ,0);
        waypoint = new Point(-1, 10);
    }

    public void Execute(@NotNull Command command){
        if (command.command() == 'N') waypoint = new Point(waypoint.x() - command.value(), waypoint.y());
        else if (command.command() == 'S') waypoint = new Point(waypoint.x() + command.value(), waypoint.y());
        else if (command.command() == 'E') waypoint = new Point(waypoint.x(), waypoint.y() + command.value());
        else if (command.command() == 'W') waypoint = new Point(waypoint.x(), waypoint.y() - command.value());
        else if (command.command() == 'R') Turn('R', command.value());
        else if (command.command() == 'L') Turn('L', command.value());
        else if (command.command() == 'F') {
            position = new Point(position.x() + waypoint.x() * command.value(), position.y() + waypoint.y() * command.value());
        }
    }

    public void Turn(Character direction, Integer degrees){
        if (direction == 'R') degrees *= -1;
        var s = Math.sin(degrees*Math.PI/180);
        var c = Math.cos(degrees*Math.PI/180);
        var nextX = (int) Math.round(waypoint.x() * c - waypoint.y() * s);
        var nextY = (int) Math.round(waypoint.x() * s + waypoint.y() * c);
        waypoint = new Point(nextX, nextY);
    }

}
