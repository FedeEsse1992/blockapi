# - Ten addresses with the most dollars in input
         SELECT *
         FROM litecoinbalances.balanceslite as balances
         ORDER BY balances.inValue DESC
         LIMIT 10;