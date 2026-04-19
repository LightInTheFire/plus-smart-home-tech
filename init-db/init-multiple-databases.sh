#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE "shopping-cart";
    CREATE DATABASE "shopping-store";
    CREATE DATABASE "warehouse";
    CREATE DATABASE "order";
    CREATE DATABASE "payment";
    CREATE DATABASE "delivery";
EOSQL
