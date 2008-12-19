module Graph

data Graph graph(NodeList nodes, 
                 EdgeList edges, 
                 AttributeList attributes);

type list[Node] Nodelist;

data Node node(NodeId id,  
               AttributeList attributes);

data Node subgraph(NodeId id, 
                   NodeList nodes, 
                   EdgeList edges, 
                   AttributeList attributes);

data NodeId id(term id);
 
type list[Attribute] AttributeList;

data Attribute bounding-box(Point first, Point second);
data Attribute color(Color color);
data Attribute curve-points(Polygon points);
data Attribute direction(Direction direction);
data Attribute fill-color(Color color);
data Attribute info(str key, value val);
data Attribute label(str label);
data Attribute tooltip(str tooltip);
data Attribute location(int x, int y);
data Attribute shape(Shape shape);
data Attribute size(int width, int height);
data Attribute style(Style style);
data Attribute level(str level);
data Attribute \file(File \file);
data Attribute \file(value \file);

data Color rgb(int red, int green, int blue);

data Style bold | dashed | dotted | filled | invisible | solid;

data Shape box | circle | diamond | egg | elipse | hexagon | 
           house | octagon | parallelogram | plaintext | 
           trapezium | triangle;

data Direction forward | back | both | none;

type list[Edge] Edgelist;

data Edge edge(NodeId from, 
               NodeId to, 
               AttributeList attributes);

type list[Point] Polygon;

data Point point(int x, int y);