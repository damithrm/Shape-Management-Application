const ShapeTable = ({ data, onEdit, onDelete }) => (
    <table className="w-full border">
      <thead>
        <tr className="bg-gray-100">
          <th>ID</th><th>Name</th><th>Type</th><th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {data.map(shape => (
          <tr key={shape.shapeId}>
            <td>{shape.shapeId}</td>
            <td>{shape.name}</td>
            <td>{shape.type}</td>
            <td>
              <button onClick={() => onEdit(shape)} className="text-blue-500 mr-2">Edit</button>
              <button onClick={() => onDelete(shape.shapeId)} className="text-red-500">Delete</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
  export default ShapeTable;
  