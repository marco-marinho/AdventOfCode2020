package Day21;

import java.util.*;

import static Helpers.Util.readFile;
import static java.lang.System.out;
public class Day21 {

    public static void main(String[] args) {
        var data = readFile("Data/Day21.txt");

        var allergIngred = new HashMap<String, HashSet<String>>();
        var recipes = new ArrayList<Recipe>();
        var ingredientSet = new HashSet<String>();
        var allergenSet = new HashSet<String>();

        for (var line : data) {
            var pieces = line.split(" \\(");
            var ingredients = pieces[0].split(" ");
            var allergens = pieces[1].replace(")", "").replace("contains", "").trim().split(", ");
            var curAllergenSet = new HashSet<>(List.of(allergens));
            allergenSet.addAll(curAllergenSet);
            ingredientSet.addAll(List.of(ingredients));
            recipes.add(new Recipe(new HashSet<>(List.of(ingredients)), new HashSet<>(curAllergenSet)));

            for (var allergen : curAllergenSet) {
                if (!allergIngred.containsKey(allergen)) allergIngred.put(allergen, new HashSet<>());
                for (var ingredient : ingredients) {
                    allergIngred.get(allergen).add(ingredient);
                }
            }

        }
        for (var recipe : recipes){
            for (var allergen : recipe.allergens()){
                allergIngred.get(allergen).retainAll(recipe.ingredients());
            }
        }
        var noAllergenSet = new HashSet<>(ingredientSet);
        for (var entry : allergIngred.entrySet()){
            noAllergenSet.removeAll(entry.getValue());
        }

        var sum = 0L;
        for (var recipe : recipes){
            sum += recipe.ingredients().stream().filter(noAllergenSet::contains).count();
        }
        out.println("Task 01: " + sum);

        var resolved = new HashMap<String, String>();
        var size = allergIngred.size();
        while(resolved.size() != size) {
            var toRemove = allergIngred.entrySet().stream().filter(c -> c.getValue().size() == 1).iterator().next();
            var toRemoveAllerg = toRemove.getKey();
            var toRemoveIngred = toRemove.getValue().iterator().next();
            resolved.put(toRemove.getKey(), toRemoveIngred);
            allergIngred.remove(toRemoveAllerg);
            for (var entry : allergIngred.entrySet()){
                entry.getValue().remove(toRemoveIngred);
            }
        }

        var orderedAllergens = new ArrayList<>(allergenSet.stream().toList());
        Collections.sort(orderedAllergens);
        var sb = new StringBuilder();
        for (var entry : orderedAllergens){
            sb.append(resolved.get(entry));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);

        out.println("Task 02: " + sb);
    }
}

record Recipe(HashSet<String> ingredients, HashSet<String> allergens){}