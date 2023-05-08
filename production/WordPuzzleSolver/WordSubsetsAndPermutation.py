import itertools


def main():
    """
    Creates a combination of words in a subset format
    Example:
        Input = 123 -> [3 1 2 23 21 31 13 32 12 132 231 123 321 213 312]
    """
    all_combinations = combinations()
    all_combinations = sorted(all_combinations, key=lambda x: len(x))
    for combination in all_combinations:
        print(combination, end=" ")


def combinations() -> set:
    """
    Creates permutations from the word given and uses a set to pick out only the unique permutations
    :rtype:
        Set: Set of all permutations
    """
    all_combinations = set()
    wordToPermute = (input().replace("", " ").split())
    for subCardinality in range(0, len(wordToPermute) + 1):
        for permutations in itertools.permutations(wordToPermute, subCardinality):
            all_combinations.add("".join(permutations).title())

    return all_combinations


if __name__ == '__main__':
    main()
