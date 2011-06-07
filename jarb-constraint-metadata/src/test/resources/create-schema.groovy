databaseChangeLog() {

	changeSet(author: "jeroen@42.nl", id: "1") {
		comment("UPGRADE: Create domain tables for testing.")

		createTable(tableName: "cars") {
			column(name: "id", type: "bigint", autoIncrement: true) {
				constraints(
					nullable: false,
					primaryKey: true,
					primaryKeyName: "pk_cars_id"
				)
			}
			column(name: "license_number", type: "varchar(6)") { constraints(nullable: false) }
			column(name: "price", type: "decimal(6,2)")
			column(name: "owner_id", type: "bigint")
		}
		
		addUniqueConstraint(tableName: "cars", columnNames: "license_number", constraintName: "uk_cars_license_number")
		
		createTable(tableName: "persons") {
			column(name: "id", type: "bigint", autoIncrement: true) {
				constraints(
					nullable: false,
					primaryKey: true,
					primaryKeyName: "pk_persons_id"
				)
			}
			column(name: "name", type: "varchar(255)") { constraints(nullable: false) }
			column(name: "age", type: "bigint")
		}
	}
	
	changeSet(author: "jeroen@42.nl", id: "2") {
		comment("UPGRADE: Create database constraint tables.")
		
		createTable(tableName: "named_constraint_metadata") {
			column(name: "name", type: "varchar(255)") {
				constraints(
					nullable: false,
					primaryKey: true,
					primaryKeyName: "pk_named_constraint_name"
				)
			}
			column(name: "type", type: "varchar(255)") { constraints(nullable: false) }
		}
	}
	
}
